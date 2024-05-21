package com.azrul.kelichap.autocomplete;

import java.util.List;
import java.util.Objects;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasTheme;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Synchronize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.shared.HasAllowedCharPattern;
import com.vaadin.flow.component.shared.HasPrefix;
import com.vaadin.flow.component.shared.HasSuffix;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextArea;
//import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

import elemental.json.JsonArray;
import elemental.json.JsonFactory;
import elemental.json.impl.JreJsonFactory;
import org.apache.commons.lang3.StringUtils;

/*
 * #%L
 * Kelichap Autocomplete for Vaadin 24
 * %%
 * Copyright (C) 2024 Azrul Hasni MADISA
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 * Server-side component for the <code>kelichap-autocomplete</code> element.
 * <p>
 * Note: isOpened,setOpened and setValue are not supported. The current
 * implementation of the polymer-side component does not allow it.
 *
 * @author Vaadin Ltd
 */
@Tag("kelichap-autocomplete")
@NpmPackage(value = "@lit-labs/observers", version = "2.0.0")
//@NpmPackage(value = "@vaadin-component-factory/vcf-autocomplete", version = "24.1.6")
@JsModule("./kelichap-autocomplete.js")
public class Autocomplete extends LitTemplate implements HasTheme, HasSize,
        HasValue<Autocomplete.AutocompleteValueAppliedEvent, String>,
        Focusable<Autocomplete>, HasValidation, HasPrefix, HasSuffix, HasAllowedCharPattern {
    private static final String OPTIONS = "options";
    private static final String TEXTAREA_SELECTOR = "this._textArea";
    private static final String LIMIT_PROP = "limit";
    private static final String READONLY_PROP = "readonly";
    private static final String LABEL_PROP = "label";
    private static final String OPENER_CHAR = "openerChar";
    private static final String PLACEHOLDER_PROP = "placeholder";
    private static final String VALUE_PROP = "value";
    private static final String ENABLED_PROP = "enabled";

    private boolean invalid;
    private boolean readOnly;
    private boolean requiredIndicatorVisible;

    private String errorMessage;
    private Boolean tokenCanContainSpace=true;
  

    @Id("textArea")
    private TextArea textArea;

    public Autocomplete(String openerChar, Boolean tokenCanContainSpace) {
        this.readOnly = false;
         setOpenerChar(openerChar);
         this.tokenCanContainSpace=tokenCanContainSpace;
    }

    public Autocomplete(int limit, String openerChar, Boolean tokenCanContainSpace) {
        this(openerChar,tokenCanContainSpace);
        setLimit(limit);
    }

    public void setLimit(int limit) {
        getElement().setProperty(LIMIT_PROP, limit);
    }

    @Override
    public void setErrorMessage(String s) {
        this.errorMessage = errorMessage;
        getElement().executeJs(String.format("%s.errorMessage=$0", TEXTAREA_SELECTOR), errorMessage);
    }

    @Override
    public String getErrorMessage() {
        return Objects.nonNull(this.errorMessage) ? this.errorMessage : "";
    }

    @Override
    public void setInvalid(boolean b) {
        this.invalid = invalid;
        getElement().executeJs(String.format("%s.invalid=$0", TEXTAREA_SELECTOR), invalid);
    }

    @Override
    public boolean isInvalid() {
        return this.invalid;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.getElement().setProperty(ENABLED_PROP, enabled);
    }

    @Override
    public boolean isEnabled() {
        return this.getElement().getProperty(ENABLED_PROP, true);
    }

    public String getPlaceholderString() {
        return getElement().getProperty(PLACEHOLDER_PROP);
    }

    public void setPlaceholder(String placeholder) {
        getElement().setProperty(PLACEHOLDER_PROP,
                placeholder == null ? "" : placeholder);
    }

    public String getLabel() {
        return getElement().getProperty(LABEL_PROP);
    }


    public void setLabel(String label) {
        getElement().setProperty(LABEL_PROP, label == null ? "" : label);
    }
    
     public String getOpenerChar() {
        return getElement().getProperty(OPENER_CHAR);
    }


    public void setOpenerChar(String openerChar) {
        getElement().setProperty(OPENER_CHAR, openerChar == null ? "" : String.valueOf(openerChar.charAt(0)));
    }

    public void setOptions(List<String> options) {
        JsonFactory jsonFactory = new JreJsonFactory();
        JsonArray jsonArray = jsonFactory.createArray();
        for (int i = 0; i < options.size(); i++) {
            if (this.tokenCanContainSpace){
                jsonArray.set(i, "["+options.get(i)+"]");
            }else{
                jsonArray.set(i, options.get(i));
            }
        }
        getElement().setPropertyJson(OPTIONS, jsonArray);
    }

    public Registration addChangeListener(
            ComponentEventListener<AucompleteChangeEvent> listener) {
        return addListener(AucompleteChangeEvent.class, listener);
    }
    
    public Registration addTokenListener(
            ComponentEventListener<AucompleteChangeEventPerToken> listener) {
        return addListener(AucompleteChangeEventPerToken.class, listener);
    }

    @Override
    public Registration addValueChangeListener(
            HasValue.ValueChangeListener<? super AutocompleteValueAppliedEvent> listener) {
        return addAutocompleteValueAppliedListener(listener::valueChanged);
    }
    
    @DomEvent("value-changed")
    public static class AucompleteChangeEvent
            extends ComponentEvent<Autocomplete> {

        private final String value;

        public AucompleteChangeEvent(Autocomplete source, boolean fromClient,
                                     @EventData("event.detail.value") String value) {
            super(source, fromClient);
            this.value = value;
            
        }

        public String getValue() {
            return value;
        }

    }

    @DomEvent("kelichap-autocomplete-value-token")
    public static class AucompleteChangeEventPerToken
            extends ComponentEvent<Autocomplete> {

        private final String token;

        public AucompleteChangeEventPerToken(Autocomplete source, boolean fromClient,
                                     @EventData("event.detail.token") String token) {
            super(source, fromClient);
            this.token = token;
            
        }

        public String getToken() {
            return token;
        }

    }

    @DomEvent("kelichap-autocomplete-value-applied")
    public static class AutocompleteValueAppliedEvent
            extends ComponentEvent<Autocomplete>
            implements HasValue.ValueChangeEvent<String> {

        private final String value;

        public AutocompleteValueAppliedEvent(Autocomplete source,
                                             boolean fromClient,
                                             @EventData("event.detail.value") String value) {
            super(source, fromClient);
            this.value = value;
            this.source = source;
        }

        public String getValue() {
            return value;
        }

        @Override
        public HasValue getHasValue() {
            // TODO Auto-generated method stub
            return (HasValue) source;
        }

        @Override
        public String getOldValue() {
            // TODO Auto-generated method stub
            return null;
        }

    }
    public Registration addAutocompleteValueAppliedListener(
            ComponentEventListener<AutocompleteValueAppliedEvent> listener) {
        return addListener(AutocompleteValueAppliedEvent.class, listener);
    }

    public void setValue(String value) {
       //getElement().executeJs("this._setValue(\"" + value + "\");");
       this.textArea.setValue(value);
       getElement().setProperty(VALUE_PROP,value);
    }

    @Synchronize(property = VALUE_PROP, value = "value-changed")
    public String getValue() {
        return getElement().getProperty(VALUE_PROP, "");
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        getElement().setProperty(READONLY_PROP, readOnly);
        getElement().getStyle().set("pointer-events", readOnly ? "none" : "auto");
    }

    @Override
    public boolean isReadOnly() {
        return this.readOnly;

    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        this.requiredIndicatorVisible = requiredIndicatorVisible;
        getElement().executeJs(String.format("%s.required=$0", TEXTAREA_SELECTOR), requiredIndicatorVisible);
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return this.requiredIndicatorVisible;
    }

    public Registration addValueClearListener(
            ComponentEventListener<ValueClearEvent> listener) {
        return addListener(ValueClearEvent.class, listener);
    }

    @DomEvent("clear")
    public static class ValueClearEvent extends ComponentEvent<Autocomplete> {
        public ValueClearEvent(Autocomplete source, boolean fromClient) {
            super(source, fromClient);
        }
    }


    @Override
    public void setPrefixComponent(Component component) {
        this.textArea.setPrefixComponent(component);
    }

    @Override
    public Component getPrefixComponent() {
        return this.textArea.getPrefixComponent();
    }

    @Override
    public void setSuffixComponent(Component component) {
        this.textArea.setSuffixComponent(component);
    }

    @Override
    public Component getSuffixComponent() {
        return this.textArea.getSuffixComponent();
    }

    /**
     * @return the tokenCanContainSpace
     */
    public Boolean getTokenCanContainSpace() {
        return tokenCanContainSpace;
    }
    
    public String getAllowedCharPattern() {
        return this.textArea.getAllowedCharPattern();
    }
    
    public void setAllowedCharPattern(String pattern){
        this.textArea.setAllowedCharPattern(pattern);
    }
}