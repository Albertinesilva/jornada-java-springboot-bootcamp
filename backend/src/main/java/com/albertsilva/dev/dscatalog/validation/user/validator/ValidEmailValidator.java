package com.albertsilva.dev.dscatalog.validation.user.validator;

import java.util.Hashtable;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;

import org.springframework.stereotype.Component;

import com.albertsilva.dev.dscatalog.validation.user.annotation.ValidEmail;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ValidEmailValidator
    implements ConstraintValidator<ValidEmail, String> {

  private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

  @Override
  public boolean isValid(String value,
      ConstraintValidatorContext context) {

    if (value == null || value.isBlank()) {
      return true;
    }

    String email = value.trim().toLowerCase();

    if (!isValidEmailFormat(email)) {
      return false;
    }

    String domain = extractDomain(email);

    return hasMxRecord(domain);
  }

  private boolean isValidEmailFormat(String email) {
    return email.matches(EMAIL_PATTERN);
  }

  private String extractDomain(String email) {
    return email.substring(email.indexOf("@") + 1);
  }

  private boolean hasMxRecord(String domain) {

    Hashtable<String, String> env = new Hashtable<>();

    env.put(
        "java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");

    InitialDirContext context = null;

    try {

      context = new InitialDirContext(env);

      Attributes attributes = context.getAttributes(domain,
          new String[] { "MX" });

      Attribute attribute = attributes.get("MX");

      return attribute != null && attribute.size() > 0;

    } catch (Exception e) {

      return false;

    } finally {
      closeContext(context);
    }
  }

  private void closeContext(InitialDirContext context) {

    if (context == null) {
      return;
    }

    try {
      context.close();
    } catch (Exception e) {
      // ignora erro ao fechar
    }
  }
}