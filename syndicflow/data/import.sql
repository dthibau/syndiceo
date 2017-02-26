-- Roles 
insert into OrganizationalEntity (type,id,nom) values ('Role','r_admin','Administrateur SyndicEo');
insert into OrganizationalEntity (type,id,nom) values ('Role','r_conseil','Conseil Syndical');
insert into OrganizationalEntity (type,id,nom) values ('Role','r_gestionnaire','Gestionnaire SyndicEo');
insert into OrganizationalEntity (type,id,nom) values ('Role','r_copro','Co-propriétaire');
insert into OrganizationalEntity (type,id,nom) values ('Role','r_system','Système');


insert into Specialite (code,nom) values ('ASC','Ascenseur');
insert into SousSpecialite (code,nom,specialite_code) values ('ASC_POR','Porte palière','ASC');
insert into SousSpecialite (code,nom,specialite_code) values ('ASC_LUM','Lumière','ASC');
insert into SousSpecialite (code,nom,specialite_code) values ('ASC_ALA','Alarme','ASC');

insert into Specialite (code,nom) values ('EAU','Eau');
insert into SousSpecialite (code,nom,specialite_code) values ('EAU_CAN','Canalisation/eau sanitaire','EAU');
insert into SousSpecialite (code,nom,specialite_code) values ('EAU_FUI','Recherche de fuite','EAU');
insert into SousSpecialite (code,nom,specialite_code) values ('EAU_ETA','Etanchéité','EAU');
insert into SousSpecialite (code,nom,specialite_code) values ('EAU_PLO','Plomberie','EAU');

insert into Specialite (code,nom) values ('ELE','Electricité');
insert into SousSpecialite (code,nom,specialite_code) values ('ELE_COU','Coupure','ELE');
insert into SousSpecialite (code,nom,specialite_code) values ('ELE_AMP','Ampoule','ELE');
insert into SousSpecialite (code,nom,specialite_code) values ('ELE_LUM','Luminaires','ELE');

insert into Specialite (code,nom) values ('MEN','Ménage');

insert into Specialite (code,nom) values ('BAL','Boite à lettres');

insert into Specialite (code,nom) values ('DEC','Elèments décoration');
insert into SousSpecialite (code,nom,specialite_code) values ('DEC_CAR','Carrelage','DEC');
insert into SousSpecialite (code,nom,specialite_code) values ('DEC_MOQ','Moquette/Revêtement sol/Parquet','DEC');
insert into SousSpecialite (code,nom,specialite_code) values ('DEC_MAR','Marbrerie/Pierre','DEC');
insert into SousSpecialite (code,nom,specialite_code) values ('DEC_PEI','Peinture','DEC');
insert into SousSpecialite (code,nom,specialite_code) values ('DEC_PAP','Papier peint','DEC');
insert into SousSpecialite (code,nom,specialite_code) values ('DEC_BOI','Boiserie','DEC');

insert into Specialite (code,nom) values ('SEC','Sécurité');
insert into SousSpecialite (code,nom,specialite_code) values ('SEC_DIG','Digicode','SEC');
insert into SousSpecialite (code,nom,specialite_code) values ('SEC_INT','Interphone','SEC');
insert into SousSpecialite (code,nom,specialite_code) values ('SEC_SYS','Système vidéo','SEC');
insert into SousSpecialite (code,nom,specialite_code) values ('SEC_ALA','Alarme','SEC');

insert into Specialite (code,nom) values ('OUV','Ouvertures (portes / fenêtre)');
insert into SousSpecialite (code,nom,specialite_code) values ('OUV_FEN','Fenêtre','OUV');
insert into SousSpecialite (code,nom,specialite_code) values ('OUV_HUI','Huisserie','OUV');
insert into SousSpecialite (code,nom,specialite_code) values ('OUV_VOL','Volet/Stores','OUV');
insert into SousSpecialite (code,nom,specialite_code) values ('OUV_PIM','Porte entrée immeuble','OUV');
insert into SousSpecialite (code,nom,specialite_code) values ('OUV_PVO','Porte voiture parking','OUV');
insert into SousSpecialite (code,nom,specialite_code) values ('OUV_PPI','Porte piéton parking','OUV');

insert into Specialite (code,nom) values ('TOI','Toiture');
insert into SousSpecialite (code,nom,specialite_code) values ('TOI_COU','Couverture','TOI');
insert into SousSpecialite (code,nom,specialite_code) values ('TOI_CHA','Charpente','TOI');

insert into Specialite (code,nom) values ('CHA','Chauffage');
insert into SousSpecialite (code,nom,specialite_code) values ('CHA_CHA','Chaudière','CHA');
insert into SousSpecialite (code,nom,specialite_code) values ('CHA_THE','Thermostat','CHA');
insert into SousSpecialite (code,nom,specialite_code) values ('CHA_RAD','Radiateur','CHA');

insert into Specialite (code,nom) values ('GAZ','Gaz');

insert into Specialite (code,nom) values ('DFU','Désenfumage');

insert into Specialite (code,nom) values ('DIN','Désinfection/Désinsectisation');

insert into Specialite (code,nom) values ('MAC','Maçonnerie/Gros œuvre');

insert into Specialite (code,nom) values ('JAR','Jardin/Espaces verts');

insert into Specialite (code,nom) values ('PIS','Entretien piscine');

insert into Specialite (code,nom) values ('DIA','Diagnostics');
insert into SousSpecialite (code,nom,specialite_code) values ('DIA_PLO','Recherche plomb','DIA');
insert into SousSpecialite (code,nom,specialite_code) values ('DIA_AMI','recherche Amiante','DIA');
insert into SousSpecialite (code,nom,specialite_code) values ('DIA_TER','Recherche termites','DIA');

insert into Specialite (code,nom) values ('AUT','Autres');

insert into Horaire (id,nom) values (1,'8h00 - 10h00');
insert into Horaire (id,nom) values (2,'10h00 - 12h00');
insert into Horaire (id,nom) values (3,'12h00 - 14h00');
insert into Horaire (id,nom) values (4,'14h00 - 16h00');
insert into Horaire (id,nom) values (5,'16h00 -18h00');
insert into Horaire (id,nom) values (6,'NA');

insert into Localisation (code,nom) values ('LOG','loge concierge');
insert into Localisation (code,nom) values ('ESC','escalier');
insert into Localisation (code,nom) values ('PAL','palier');
insert into Localisation (code,nom) values ('SOU','sous-sol');
insert into Localisation (code,nom) values ('RDC','RDC');
insert into Localisation (code,nom) values ('ET1','Etage 1');
insert into Localisation (code,nom) values ('ET2','Etage 2');
insert into Localisation (code,nom) values ('ET3','Etage 3');
insert into Localisation (code,nom) values ('ET4','Etage 4');
insert into Localisation (code,nom) values ('ET5','Etage 5');
insert into Localisation (code,nom) values ('ET6','Etage 6');
insert into Localisation (code,nom) values ('ETS','Etages supérieurs');
insert into Localisation (code,nom) values ('CAV','cave');
insert into Localisation (code,nom) values ('PIN','parking intérieur');
insert into Localisation (code,nom) values ('PEX','parking extérieur');
insert into Localisation (code,nom) values ('VOR','vide ordure');
insert into Localisation (code,nom) values ('LOV','local vélo');
insert into Localisation (code,nom) values ('LAU','autre local');

-- Criteres
insert into Critere (code,nom) values ('GAR','Gardienne');
insert into SousCritere (code,nom,critere_code) values ('GAR_CON','Congés','GAR');
insert into SousCritere (code,nom,critere_code) values ('GAR_HOR','Horaires','GAR');
insert into SousCritere (code,nom,critere_code) values ('GAR_COU','courrier / colis','GAR');
insert into SousCritere (code,nom,critere_code) values ('GAR_MEN','Ménage','GAR');
insert into SousCritere (code,nom,critere_code) values ('GAR_POU','Poubelle','GAR');
insert into SousCritere (code,nom,critere_code) values ('GAR_ACC','Acccès immeuble','GAR');
insert into SousCritere (code,nom,critere_code) values ('GAR_CLE','Clé','GAR');
insert into SousCritere (code,nom,critere_code) values ('GAR_AUT','Autres','GAR');

insert into Critere (code,nom) values ('PRI','Parties privatives');
insert into SousCritere (code,nom,critere_code) values ('PRI_DEG','Dégât des eaux','PRI');
insert into SousCritere (code,nom,critere_code) values ('PRI_APP','Appel de fonds','PRI');
insert into SousCritere (code,nom,critere_code) values ('PRI_DEM','Demande de document','PRI');
insert into SousCritere (code,nom,critere_code) values ('PRI_AUT','Autres','PRI');

insert into Critere (code,nom) values ('LOC','Location');
insert into SousCritere (code,nom,critere_code) values ('LOC_ARR','Arrivée nouveau locataire','LOC');
insert into SousCritere (code,nom,critere_code) values ('LOC_DEP','Départ locataire','LOC');
insert into SousCritere (code,nom,critere_code) values ('LOC_LOY','Loyers','LOC');
insert into SousCritere (code,nom,critere_code) values ('LOC_CHA','Charges','LOC');

insert into Critere (code,nom) values ('COM','Comptable');
insert into SousCritere (code,nom,critere_code) values ('COM_APP','Appel de charge','COM');
insert into SousCritere (code,nom,critere_code) values ('COM_DOC','Document AG','COM');
	
