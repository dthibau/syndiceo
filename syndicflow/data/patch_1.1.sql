alter table demande alter column description TYPE varchar(65025);

update demande set confidentielle='false' where confidentielle is null;

insert into specialite (code,nom) values ('HAL','Hall d''entrée');