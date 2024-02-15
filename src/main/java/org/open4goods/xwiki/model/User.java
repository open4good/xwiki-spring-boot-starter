
package org.open4goods.xwiki.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "links",
    "type",
    "id",
    "pageFullName",
    "title",
    "wiki",
    "space",
    "pageName",
    "modified",
    "author",
    "authorName",
    "version",
    "language",
    "className",
    "objectNumber",
    "filename",
    "score",
    "object",
    "hierarchy"
})@Generated("jsonschema2pojo")
public class User {

    @JsonProperty("links")
    private List<Link> links = new ArrayList<Link>();
    @JsonProperty("type")
    private String type;
    @JsonProperty("id")
    private String id;
    @JsonProperty("pageFullName")
    private String pageFullName;
    @JsonProperty("title")
    private String title;
    @JsonProperty("wiki")
    private String wiki;
    @JsonProperty("space")
    private String space;
    @JsonProperty("pageName")
    private String pageName;
    @JsonProperty("modified")
    private Long modified;
    @JsonProperty("author")
    private String author;
    @JsonProperty("authorName")
    private String authorName;
    @JsonProperty("version")
    private String version;
    @JsonProperty("language")
    private String language;
    @JsonProperty("className")
    private String className;
    @JsonProperty("objectNumber")
    private String objectNumber;
    @JsonProperty("filename")
    private String filename;
    @JsonProperty("score")
    private Double score;
    @JsonProperty("object")
    private String object;
    @JsonProperty("hierarchy")
    private String hierarchy;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("links")
    public List<Link> getLinks() {
        return links;
    }

    @JsonProperty("links")
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public User withLinks(List<Link> links) {
        this.links = links;
        return this;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    public User withType(String type) {
        this.type = type;
        return this;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public User withId(String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("pageFullName")
    public String getPageFullName() {
        return pageFullName;
    }

    @JsonProperty("pageFullName")
    public void setPageFullName(String pageFullName) {
        this.pageFullName = pageFullName;
    }

    public User withPageFullName(String pageFullName) {
        this.pageFullName = pageFullName;
        return this;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public User withTitle(String title) {
        this.title = title;
        return this;
    }

    @JsonProperty("wiki")
    public String getWiki() {
        return wiki;
    }

    @JsonProperty("wiki")
    public void setWiki(String wiki) {
        this.wiki = wiki;
    }

    public User withWiki(String wiki) {
        this.wiki = wiki;
        return this;
    }

    @JsonProperty("space")
    public String getSpace() {
        return space;
    }

    @JsonProperty("space")
    public void setSpace(String space) {
        this.space = space;
    }

    public User withSpace(String space) {
        this.space = space;
        return this;
    }

    @JsonProperty("pageName")
    public String getPageName() {
        return pageName;
    }

    @JsonProperty("pageName")
    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public User withPageName(String pageName) {
        this.pageName = pageName;
        return this;
    }

    @JsonProperty("modified")
    public Long getModified() {
        return modified;
    }

    @JsonProperty("modified")
    public void setModified(Long modified) {
        this.modified = modified;
    }

    public User withModified(Long modified) {
        this.modified = modified;
        return this;
    }

    @JsonProperty("author")
    public String getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(String author) {
        this.author = author;
    }

    public User withAuthor(String author) {
        this.author = author;
        return this;
    }

    @JsonProperty("authorName")
    public Object getAuthorName() {
        return authorName;
    }

    @JsonProperty("authorName")
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public User withAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    public User withVersion(String version) {
        this.version = version;
        return this;
    }

    @JsonProperty("language")
    public Object getLanguage() {
        return language;
    }

    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    public User withLanguage(String language) {
        this.language = language;
        return this;
    }

    @JsonProperty("className")
    public String getClassName() {
        return className;
    }

    @JsonProperty("className")
    public void setClassName(String className) {
        this.className = className;
    }

    public User withClassName(String className) {
        this.className = className;
        return this;
    }

    @JsonProperty("objectNumber")
    public Object getObjectNumber() {
        return objectNumber;
    }

    @JsonProperty("objectNumber")
    public void setObjectNumber(String objectNumber) {
        this.objectNumber = objectNumber;
    }

    public User withObjectNumber(String objectNumber) {
        this.objectNumber = objectNumber;
        return this;
    }

    @JsonProperty("filename")
    public Object getFilename() {
        return filename;
    }

    @JsonProperty("filename")
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public User withFilename(String filename) {
        this.filename = filename;
        return this;
    }

    @JsonProperty("score")
    public Double getScore() {
        return score;
    }

    @JsonProperty("score")
    public void setScore(Double score) {
        this.score = score;
    }

    public User withScore(Double score) {
        this.score = score;
        return this;
    }

    @JsonProperty("object")
    public Object getObject() {
        return object;
    }

    @JsonProperty("object")
    public void setObject(String object) {
        this.object = object;
    }

    public User withObject(String object) {
        this.object = object;
        return this;
    }

    @JsonProperty("hierarchy")
    public String getHierarchy() {
        return hierarchy;
    }

    @JsonProperty("hierarchy")
    public void setHierarchy(String hierarchy) {
        this.hierarchy = hierarchy;
    }

    public User withHierarchy(String hierarchy) {
        this.hierarchy = hierarchy;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public User withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(User.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("links");
        sb.append('=');
        sb.append(((this.links == null)?"<null>":this.links));
        sb.append(',');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
        sb.append(',');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("pageFullName");
        sb.append('=');
        sb.append(((this.pageFullName == null)?"<null>":this.pageFullName));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null)?"<null>":this.title));
        sb.append(',');
        sb.append("wiki");
        sb.append('=');
        sb.append(((this.wiki == null)?"<null>":this.wiki));
        sb.append(',');
        sb.append("space");
        sb.append('=');
        sb.append(((this.space == null)?"<null>":this.space));
        sb.append(',');
        sb.append("pageName");
        sb.append('=');
        sb.append(((this.pageName == null)?"<null>":this.pageName));
        sb.append(',');
        sb.append("modified");
        sb.append('=');
        sb.append(((this.modified == null)?"<null>":this.modified));
        sb.append(',');
        sb.append("author");
        sb.append('=');
        sb.append(((this.author == null)?"<null>":this.author));
        sb.append(',');
        sb.append("authorName");
        sb.append('=');
        sb.append(((this.authorName == null)?"<null>":this.authorName));
        sb.append(',');
        sb.append("version");
        sb.append('=');
        sb.append(((this.version == null)?"<null>":this.version));
        sb.append(',');
        sb.append("language");
        sb.append('=');
        sb.append(((this.language == null)?"<null>":this.language));
        sb.append(',');
        sb.append("className");
        sb.append('=');
        sb.append(((this.className == null)?"<null>":this.className));
        sb.append(',');
        sb.append("objectNumber");
        sb.append('=');
        sb.append(((this.objectNumber == null)?"<null>":this.objectNumber));
        sb.append(',');
        sb.append("filename");
        sb.append('=');
        sb.append(((this.filename == null)?"<null>":this.filename));
        sb.append(',');
        sb.append("score");
        sb.append('=');
        sb.append(((this.score == null)?"<null>":this.score));
        sb.append(',');
        sb.append("object");
        sb.append('=');
        sb.append(((this.object == null)?"<null>":this.object));
        sb.append(',');
        sb.append("hierarchy");
        sb.append('=');
        sb.append(((this.hierarchy == null)?"<null>":this.hierarchy));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.author == null)? 0 :this.author.hashCode()));
        result = ((result* 31)+((this.wiki == null)? 0 :this.wiki.hashCode()));
        result = ((result* 31)+((this.hierarchy == null)? 0 :this.hierarchy.hashCode()));
        result = ((result* 31)+((this.objectNumber == null)? 0 :this.objectNumber.hashCode()));
        result = ((result* 31)+((this.language == null)? 0 :this.language.hashCode()));
        result = ((result* 31)+((this.className == null)? 0 :this.className.hashCode()));
        result = ((result* 31)+((this.type == null)? 0 :this.type.hashCode()));
        result = ((result* 31)+((this.title == null)? 0 :this.title.hashCode()));
        result = ((result* 31)+((this.pageName == null)? 0 :this.pageName.hashCode()));
        result = ((result* 31)+((this.version == null)? 0 :this.version.hashCode()));
        result = ((result* 31)+((this.space == null)? 0 :this.space.hashCode()));
        result = ((result* 31)+((this.pageFullName == null)? 0 :this.pageFullName.hashCode()));
        result = ((result* 31)+((this.score == null)? 0 :this.score.hashCode()));
        result = ((result* 31)+((this.filename == null)? 0 :this.filename.hashCode()));
        result = ((result* 31)+((this.authorName == null)? 0 :this.authorName.hashCode()));
        result = ((result* 31)+((this.modified == null)? 0 :this.modified.hashCode()));
        result = ((result* 31)+((this.links == null)? 0 :this.links.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.object == null)? 0 :this.object.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof User) == false) {
            return false;
        }
        User rhs = ((User) other);
        return (((((((((((((((((((((this.author == rhs.author)||((this.author!= null)&&this.author.equals(rhs.author)))&&((this.wiki == rhs.wiki)||((this.wiki!= null)&&this.wiki.equals(rhs.wiki))))&&((this.hierarchy == rhs.hierarchy)||((this.hierarchy!= null)&&this.hierarchy.equals(rhs.hierarchy))))&&((this.objectNumber == rhs.objectNumber)||((this.objectNumber!= null)&&this.objectNumber.equals(rhs.objectNumber))))&&((this.language == rhs.language)||((this.language!= null)&&this.language.equals(rhs.language))))&&((this.className == rhs.className)||((this.className!= null)&&this.className.equals(rhs.className))))&&((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type))))&&((this.title == rhs.title)||((this.title!= null)&&this.title.equals(rhs.title))))&&((this.pageName == rhs.pageName)||((this.pageName!= null)&&this.pageName.equals(rhs.pageName))))&&((this.version == rhs.version)||((this.version!= null)&&this.version.equals(rhs.version))))&&((this.space == rhs.space)||((this.space!= null)&&this.space.equals(rhs.space))))&&((this.pageFullName == rhs.pageFullName)||((this.pageFullName!= null)&&this.pageFullName.equals(rhs.pageFullName))))&&((this.score == rhs.score)||((this.score!= null)&&this.score.equals(rhs.score))))&&((this.filename == rhs.filename)||((this.filename!= null)&&this.filename.equals(rhs.filename))))&&((this.authorName == rhs.authorName)||((this.authorName!= null)&&this.authorName.equals(rhs.authorName))))&&((this.modified == rhs.modified)||((this.modified!= null)&&this.modified.equals(rhs.modified))))&&((this.links == rhs.links)||((this.links!= null)&&this.links.equals(rhs.links))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.object == rhs.object)||((this.object!= null)&&this.object.equals(rhs.object))));
    }

}
