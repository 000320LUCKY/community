package lucky.yc.community.dto;

import lombok.Data;

@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatar_url;

    public GithubUser(String name, Long id, String bio, String avatar_url) {
        this.name = name;
        this.id = id;
        this.bio = bio;
        this.avatar_url = avatar_url;
    }

    public GithubUser() {
    }

    @Override
    public String toString() {
        return "GithubUser{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", bio='" + bio + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                '}';
    }
}
