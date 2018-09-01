package org.mouthaan.netify.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The class Actor Entity.
 * Constructors, getters and setters are generated by Lombok
 */
@Entity
@Table(name = "ntfy_actor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Actor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(notes = "The database generated Actor ID")
    private Integer id;

    @Column(name = "name")
    @ApiModelProperty(notes = "The Actor's Name")
    private String name;

    @Column(name = "gender_id")
    @ApiModelProperty(notes = "The Actor's Gender")
    private Gender gender;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Actor actor = (Actor) o;

        if (getId() != null ? !getId().equals(actor.getId()) : actor.getId() != null) return false;
        if (!getName().equals(actor.getName())) return false;
        return getGender() == actor.getGender();
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + getName().hashCode();
        result = 31 * result + getGender().hashCode();
        return result;
    }
}
