package ru.iteco;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FlatBlockTemplateMethod implements TemplateMethodModelEx {

    private final FlatBlockService flatBlockService;

    @Autowired
    public FlatBlockTemplateMethod(FlatBlockService flatBlockService) {
        this.flatBlockService = flatBlockService;
    }

    @Override
    public Object exec(List list) throws TemplateModelException {
        if (list.size() != 1) {
            throw new TemplateModelException("Wrong arguments");
        }
        SimpleScalar freemarkerArg = (SimpleScalar) list.get(0);
        String flatBlockName = freemarkerArg.getAsString();
        return flatBlockService.getContentByName(flatBlockName);
    }
}
