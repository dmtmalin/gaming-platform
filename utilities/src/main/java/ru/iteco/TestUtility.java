package ru.iteco;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class TestUtility {

    public String buildUrlEncodedFormEntity(String... params)
            throws UnsupportedEncodingException, IllegalArgumentException {
        if ((params.length % 2) > 0) {
            throw new IllegalArgumentException("Need to give an even number of parameters");
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < params.length; i += 2) {
            if (i > 0) result.append('&');
            result.append(URLEncoder.encode(params[i], UTF_8.toString()))
                    .append('=')
                    .append(URLEncoder.encode(params[i + 1], UTF_8.toString()));
        }
        return result.toString();
    }

    public TemplateMethodModelEx freemarkerEmptyMethod() {
        return new TemplateMethodModelEx() {
            @Override
            public Object exec(List list) throws TemplateModelException {
                return "freemarkerEmptyMethod";
            }
        };
    }
}
