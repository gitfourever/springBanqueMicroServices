package ang.neggaw.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorValue(value = "R")
@AllArgsConstructor
public class Retrait extends Operation{
    public Retrait(Date dateOperation, double montant,
                   Compte compte, Employe employe) {
        super(dateOperation, montant, compte, employe);
    }
}
