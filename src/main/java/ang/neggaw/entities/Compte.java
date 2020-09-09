package ang.neggaw.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "comptes")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "typeCte", length = 2, discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "typeCompte")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "CC", value = CompteCourant.class),
        @JsonSubTypes.Type(name = "CE", value = CompteEpargne.class),
})
@XmlSeeAlso(value = {
        CompteCourant.class,
        CompteEpargne.class
})
public abstract class Compte implements Serializable {

    @Id
    @Column(name = "numCte", length = 24)
    private String numCte;

    @Transient
    private String typeCte;

    @Column(name = "solde")
    private double solde;

    @Column(name = "dateCreation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    @ManyToOne
    @JoinColumn(name = "id_employe")
    private Employe employe;

    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client client;

    @OneToMany(mappedBy = "compte")
    @XmlTransient
    @JsonIgnore
    private Collection<Operation> operations;

    // contructeur
    public Compte(String numCte, Date dateCreation, double solde, Client client, Employe employe) {
        this.numCte = numCte;
        this.dateCreation = dateCreation;
        this.solde = solde;
        this.client = client;
        this.employe = employe;
    }
}