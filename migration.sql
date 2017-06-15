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
alter table demande rename column horairesouhaite_id to horaire_souhaite_id;

alter table demande_conseils rename column demande_id to intervention_id;

alter table event rename column autreemails to autre_emails;
alter table event rename column taskcode to task_code;

alter table event_pourinfo rename column pourinfo_id to pour_info_id;

alter table fichier_event rename column contenttype to content_type;
alter table fichier_event rename column filename to file_name;
alter table fichier_event rename column uploaddate to upload_date;

alter table immeuble rename column nointervention to no_intervention;

alter table piece_jointe rename column contenttype to content_type;
alter table piece_jointe rename column filename to file_name;
alter table piece_jointe rename column uploaddate to upload_date;

alter table planification rename column horairelibre to horaire_libre;

alter table session_info rename column lastmodificationdate to last_modification_date;
alter table session_info rename column rulesbytearray to rules_byte_array;
alter table session_info rename column startdate to start_date;

alter table task rename column actorid to actor_id;
alter table task rename column processinstanceid to process_instance_id;
alter table task rename column subprocessinstanceid to sub_process_instance_id;
alter table task rename column workitemid to work_item_id;

alter table work_item_info rename column workitemid to work_item_id;;
alter table work_item_info rename column creationdate to creation_date;
alter table work_item_info rename column processinstanceid to process_instance_id;
alter table work_item_info rename column workitembytearray to work_item_byte_array;

 

