Kelichap Autocomplete
=====================

 

This project is a Vaadin autocomplete component inspired by
<https://github.com/vaadin-component-factory/vcf-autocomplete>

 

Usage example
-------------

 

The example below create TextArea where tags can be typed. Tags will only appear
at when the “\#” key is typed

 

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
List<String> allTags = List.of("#BOBOIBOY", "#EJENALI", "#MECHAMATO);  
Autocomplete acTags = new Autocomplete("#", false);
acTags.setLabel("Tags");
acTags.setValue(StringUtils.join(currentTags, " "));
acTags.setWidthFull();
acTags.setAllowedCharPattern(ALLOWED_TAGS_PATTERN);

acTags.addTokenListener(event -> {
    String text = event.getToken();
    if (text != null) {
        List<String> selectedTags = allTags.stream().filter(u -> {
            return StringUtils.containsIgnoreCase(u, text.trim().replaceAll("[^\\p{L}\\p{Nd}]+", ""));
        }).collect(Collectors.toList());
        acTags.setOptions(selectedTags);
    }
});
return acTags;
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 

Support 
--------

The software and documentation here is distributed as-is. No support will be
provided
