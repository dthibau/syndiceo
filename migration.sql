alter table demande_organizationalentity rename to demande_conseils;
alter table event_fichierevent rename to event_fichiers;
alter table fichierevent rename to fichier_event;
alter table organizationalentity rename to organizational_entity;
alter table piecejointe rename to piece_jointe;
alter table processinstanceinfo rename to process_instance_info;
alter table sessioninfo rename to session_info;
alter table souscritere rename to sous_critere;
alter table sousspecialite rename to sous_specialite;
alter table workiteminfo rename to work_item_info;


alter table demande rename column nointervention to no_intervention;
alter table demande rename column processinstanceid to process_instance_id;
alter table demande rename column archiveddate to archived_date;
alter table demande rename column createddate to created_date;
alter table demande rename column statuscode to status_code;
alter table demande rename column contactdemandeur to contact_demandeur;
alter table demande rename column contactemail to contact_email;
alter table demande rename column contacttelephone to contact_telephone;
alter table demande rename column datesouhaitee to date_souhaitee;
alter table demande rename column pourinfo1 to pour_info_1;
alter table demande rename column pourinfo2 to pour_info_2;
alter table demande rename column pourinfo3 to pour_info_3;
alter table demande rename column horairesouhaite_id to horaire_souhaite_id
alter table demande rename column 



alter table demande_conseils rename column demande_id to intervention_id;
