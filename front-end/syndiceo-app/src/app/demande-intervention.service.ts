export class DemandeInterventionService {
 ressource  = [
                        {
                            numero: '1',
                            demandeur : 'test naziff',
                            dateCreation: '01/04/2012',
                            domaine: 'Ascenseur', 
                            objet: 'M.Local',
                            dateIntervention: '27/04/2012',
                            immeuble: '9991, 41 BD DE CRETEIL',
                            status: 'Default'
                        }, 
                        {
                             numero: '2',
                            demandeur : 'test naziff',
                            dateCreation: '01/04/2012',
                            domaine: 'Ascenseur', 
                            objet: 'M.Local',
                            dateIntervention: '27/04/2012',
                            immeuble: '9991, 41 BD DE CRETEIL',
                            status: 'Default'
                        },
                        {
                             numero: '3',
                            demandeur : 'test naziff',
                            dateCreation: '01/04/2012',
                            domaine: 'Ascenseur', 
                            objet: 'M.Local',
                            dateIntervention: '27/04/2012',
                            immeuble: '9991, 41 BD DE CRETEIL',
                            status: 'Default'
                        }
                     ];
  

    listeDemandes(){
       
       return this.ressource; 
    }
}