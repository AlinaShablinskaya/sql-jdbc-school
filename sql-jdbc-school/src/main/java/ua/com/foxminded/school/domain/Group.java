package ua.com.foxminded.school.domain;

import java.util.Objects;

public class Group {
    private static final String SPACE = " ";
    private static final String NEW_LINE = "\n";
    
    private final Integer groupId;
    private final String groupName;
    
    public Group(Builder builder) {
        this.groupId = builder.groupId;
        this.groupName = builder.groupName;
    }
    
    public static Builder builder() {
        return new Builder();
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, groupName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Group other = (Group) obj;
        return Objects.equals(groupId, other.groupId) &&
               Objects.equals(groupName, other.groupName);
    }

    @Override
    public String toString() {
        return groupId + SPACE + groupName + NEW_LINE;
    }
    
    public static class Builder {
        private Integer groupId;
        private String groupName;
        
        public Builder withGroupId(Integer groupId) {
            this.groupId = groupId;
            return this;
        }
        
        public Builder withGroupName(String groupName) {
            this.groupName = groupName;
            return this;
        } 
        
        public Group build() {
            return new Group(this);
        }    
    }
}
