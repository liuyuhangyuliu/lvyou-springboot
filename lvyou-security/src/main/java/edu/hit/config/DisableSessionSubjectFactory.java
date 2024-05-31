package edu.hit.config;

import org.apache.shiro.mgt.DefaultSubjectFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;

public class DisableSessionSubjectFactory extends DefaultSubjectFactory {
    @Override
    public Subject createSubject(SubjectContext context) {
        context.setSessionCreationEnabled(false);
        return super.createSubject(context);
    }
}
