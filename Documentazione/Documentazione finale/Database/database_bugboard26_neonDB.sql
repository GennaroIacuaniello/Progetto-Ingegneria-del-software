--001

CREATE DOMAIN UserType AS SMALLINT;		--0 guest, 1 developer, 2 admin;

-------------------------------------------------------------------------------------------------------------------------

--002

CREATE TABLE User_ (

	user_id SERIAL PRIMARY KEY,
	email VARCHAR(50) UNIQUE NOT NULL,
	hashed_password VARCHAR(255) NOT NULL,
	user_type UserType NOT NULL DEFAULT 0,

	CONSTRAINT correct_mail_format CHECK( email LIKE '%@%.%' AND email NOT LIKE '%@%@%')
	--controllo ci sia una e una sola @ e almeno un punto

);

-------------------------------------------------------------------------------------------------------------------------

--003

CREATE TABLE Project (

	project_id SERIAL PRIMARY KEY,
	project_name TEXT NOT NULL,

	CONSTRAINT correctness_of_project_name_min_length CHECK( LENGTH(project_name) > 0 )

);

-------------------------------------------------------------------------------------------------------------------------

--004

CREATE TABLE Team (

	team_id SERIAL PRIMARY KEY,
	team_name TEXT NOT NULL,
	project_id INTEGER NOT NULL,

	CONSTRAINT project_FK FOREIGN KEY(project_id) REFERENCES Project(project_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT correctness_of_team_name_min_length CHECK( LENGTH(team_name) > 0 )

);

-------------------------------------------------------------------------------------------------------------------------

--005

CREATE TYPE IssueType AS ENUM ('BUG', 'DOCUMENTATION', 'FEATURE', 'QUESTION');

-------------------------------------------------------------------------------------------------------------------------

--006

CREATE TYPE IssueStatus AS ENUM ('ASSIGNED', 'RESOLVED', 'TODO');

-------------------------------------------------------------------------------------------------------------------------

--007

CREATE DOMAIN PriorityType AS SMALLINT;		

--0 molto bassa, 
--1 bassa, 
--2 normale, 
--3 alta, 
--4 molto alta;

-------------------------------------------------------------------------------------------------------------------------

--008

CREATE TABLE Issue (

	issue_id SERIAL PRIMARY KEY,
	title VARCHAR(100) NOT NULL,
	issue_description TEXT NOT NULL,
	issue_priority PriorityType NOT NULL DEFAULT 2,
	issue_image BYTEA,
	issue_type IssueType NOT NULL,
	issue_status IssueStatus NOT NULL DEFAULT 'TODO',
	tags TEXT,
	report_time TIMESTAMP NOT NULL,
	resolution_time TIMESTAMP,
	reporter_id INTEGER NOT NULL DEFAULT 0,
	resolver_id INTEGER DEFAULT 0,
	project_id INTEGER NOT NULL,

	CONSTRAINT reporter_FK FOREIGN KEY(reporter_id) REFERENCES User_(user_id) ON DELETE SET DEFAULT ON UPDATE CASCADE,
	CONSTRAINT resolver_FK FOREIGN KEY(resolver_id) REFERENCES User_(user_id) ON DELETE SET DEFAULT ON UPDATE CASCADE,
	CONSTRAINT project_FK FOREIGN KEY(project_id) REFERENCES Project(project_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT really_assigned CHECK( ( resolver_id IS NULL AND issue_status = 'TODO' ) OR ( resolver_id IS NOT NULL AND ( issue_status = 'ASSIGNED' OR  issue_status = 'RESOLVED') ) ),
	CONSTRAINT resolution_after_report CHECK( (resolution_time IS NULL) OR (resolution_time >= report_time) ),
	CONSTRAINT issue_priority_not_negative CHECK(issue_priority >= 0)

);

-------------------------------------------------------------------------------------------------------------------------

--009

CREATE TABLE Works_on (

	project_id INTEGER NOT NULL,
	user_id INTEGER NOT NULL,

	PRIMARY KEY (project_id, user_id),
	CONSTRAINT project_FK FOREIGN KEY(project_id) REFERENCES Project(project_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT user_FK FOREIGN KEY(user_id) REFERENCES User_(user_id) ON DELETE CASCADE ON UPDATE CASCADE

);

-------------------------------------------------------------------------------------------------------------------------

--010

CREATE TABLE Works_in (

	team_id INTEGER NOT NULL,
	user_id INTEGER NOT NULL,

	PRIMARY KEY (team_id, user_id),
	CONSTRAINT team_FK FOREIGN KEY(team_id) REFERENCES Team(team_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT user_FK FOREIGN KEY(user_id) REFERENCES User_(user_id) ON DELETE CASCADE ON UPDATE CASCADE

);

-------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------------------------------------


----------------------------------------------- TRIGGER -----------------------------------------------------------------


-------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------------------------------------

--011
--Una issue è in relazione "assigned" (resolver_id) con uno user se e solo se type vale 1 o 2

CREATE OR REPLACE FUNCTION fun_block_assign_issue_to_guests()
RETURNS TRIGGER
AS $$
DECLARE

	associated_user User_%ROWTYPE;

BEGIN
	
	SELECT * INTO associated_user
	FROM USER_ U
	WHERE U.user_id = NEW.resolver_id;

	IF associated_user.user_type <> 1 AND associated_user.user_type <> 2 THEN
			
		RAISE EXCEPTION 'L''utente % e'' un guest, non gli si puo'' assegnare una issue!', associated_user.email;

	END IF;

	RETURN NEW;

END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER block_assign_issue_to_guests
BEFORE INSERT OR UPDATE ON ISSUE
FOR EACH ROW
EXECUTE FUNCTION fun_block_assign_issue_to_guests();

-------------------------------------------------------------------------------------------------------------------------

--012
--Un Project è in relazione "works on" con uno user solo se type vale 1 o 2

CREATE OR REPLACE FUNCTION fun_block_put_guest_in_project()
RETURNS TRIGGER
AS $$
DECLARE

	associated_user USER_%ROWTYPE;

BEGIN
	
	SELECT * INTO associated_user
	FROM USER_ U
	WHERE U.user_id = NEW.user_id;

	IF associated_user.user_type <> 1 AND associated_user.user_type <> 2 THEN
			
		RAISE EXCEPTION 'L''utente % e'' un guest, non puo'' lavorare ad un progetto!', associated_user.email;

	END IF;

	RETURN NEW;

END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER block_put_guest_in_project
BEFORE INSERT OR UPDATE ON WORKS_ON
FOR EACH ROW
EXECUTE FUNCTION fun_block_put_guest_in_project();

-------------------------------------------------------------------------------------------------------------------------

--013
--Un Team è in relazione "works in" con uno user solo se type vale 1 o 2

CREATE OR REPLACE FUNCTION fun_block_put_guest_in_team()
RETURNS TRIGGER
AS $$
DECLARE

	associated_user USER_%ROWTYPE;

BEGIN
	
	SELECT * INTO associated_user
	FROM USER_ U
	WHERE U.user_id = NEW.user_id;

	IF associated_user.user_type <> 1 AND associated_user.user_type <> 2 THEN
			
		RAISE EXCEPTION 'L''utente % e'' un guest, non puo'' lavorare in un team!', associated_user.email;

	END IF;

	RETURN NEW;

END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER block_put_guest_in_team
BEFORE INSERT OR UPDATE ON WORKS_IN
FOR EACH ROW
EXECUTE FUNCTION fun_block_put_guest_in_team();

-------------------------------------------------------------------------------------------------------------------------

--014
--Non si può modificare una issue segnata come 'resolved'

CREATE OR REPLACE FUNCTION fun_block_upd_resolved_issue()
RETURNS TRIGGER
AS $$
BEGIN

	
	IF OLD.issue_status = 'RESOLVED' THEN

		RAISE EXCEPTION 'La issue % e'' gia'' stata risolta, non puo'' essere modificata!', OLD.issue_id;

	END IF;


	RETURN NEW;

END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER block_upd_resolved_issue
BEFORE UPDATE ON Issue
FOR EACH ROW
EXECUTE FUNCTION fun_block_upd_resolved_issue();

-------------------------------------------------------------------------------------------------------------------------

--015
--Non si possono modificare gli attributi issue_id, report_time e reporter_id di una issue

CREATE OR REPLACE FUNCTION fun_block_upd_id_rep_time_rep_id_issue()
RETURNS TRIGGER
AS $$
BEGIN

	
	IF OLD.issue_id <> NEW.issue_id OR OLD.report_time <> NEW.report_time OR OLD.reporter_id <> NEW.reporter_id THEN

		RAISE EXCEPTION 'Non si ponnono modificare gli attributi issue_id, report_time e reporter_id di una issue!';

	END IF;


	RETURN NEW;

END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER block_upd_id_rep_time_rep_id_issue
BEFORE UPDATE OF issue_id, report_time, reporter_id ON Issue
FOR EACH ROW
EXECUTE FUNCTION fun_block_upd_id_rep_time_rep_id_issue();

-------------------------------------------------------------------------------------------------------------------------

--016
--Quando una issue viene segnata come 'resolved', viene anche impostato il resolution_time

CREATE OR REPLACE FUNCTION fun_set_res_time_when_resolved_issue()
RETURNS TRIGGER
AS $$
BEGIN

	
	IF NEW.issue_status = 'RESOLVED' and OLD.issue_status <> 'RESOLVED' THEN

		NEW.resolution_time := CURRENT_TIMESTAMP;

	END IF;


	RETURN NEW;

END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER set_res_time_when_resolved_issue
BEFORE UPDATE OF issue_status ON Issue
FOR EACH ROW
EXECUTE FUNCTION fun_set_res_time_when_resolved_issue();

-------------------------------------------------------------------------------------------------------------------------

--017
--Può essere segnata come 'resolved' solo una issue che è stata assegnata (quindi con resolver_id not null)

CREATE OR REPLACE FUNCTION fun_resolve_issue_only_if_assigned()
RETURNS TRIGGER
AS $$
BEGIN

	
	IF NEW.issue_status = 'RESOLVED' and OLD.issue_status <> 'RESOLVED' THEN

		IF NEW.resolver_id IS NULL THEN

			RAISE EXCEPTION 'La issue % non e'' stata assegnata a nessuno, non puo'' essere risolta!', OLD.issue_id;

		END IF;

	END IF;


	RETURN NEW;

END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER resolve_issue_only_if_assigned
BEFORE UPDATE OF issue_status ON Issue
FOR EACH ROW
EXECUTE FUNCTION fun_resolve_issue_only_if_assigned();

-------------------------------------------------------------------------------------------------------------------------

--018
--Quando viene aggiunto (per la precisione, DOPO aver aggiunto) un membro ad un team (nella tabella Works_in), 
--si controlla se lo user appena aggiunto fa parte anche del progetto (nella tabella Works_on),
--in caso non sia così, lo si aggiunge

CREATE OR REPLACE FUNCTION fun_check_if_add_user_to_project_when_add_to_team()
RETURNS TRIGGER
AS $$
DECLARE

	associated_project_id INTEGER;

BEGIN
	
	SELECT T.project_id INTO associated_project_id
	FROM Team T
	WHERE T.team_id = NEW.team_id;

	IF NOT EXISTS(SELECT * FROM Works_on W
				WHERE W.user_id = NEW.user_id AND W.project_id = associated_project_id) THEN 

		INSERT INTO Works_on (project_id, user_id) VALUES
		(associated_project_id, NEW.user_id);

	END IF;

	RETURN NEW;

END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER check_if_add_user_to_project_when_add_to_team
AFTER INSERT ON Works_in
FOR EACH ROW
EXECUTE FUNCTION fun_check_if_add_user_to_project_when_add_to_team();

-------------------------------------------------------------------------------------------------------------------------

--019
--Quando viene cancellato un membro da un team (dalla tabella Works_in), si controlla se lo user è in altri
--team del progetto (sempre nella tabella Works_in), in caso non sia così, lo si toglie anche dal relativo progetto
--(dalla tabella Works_on)

CREATE OR REPLACE FUNCTION fun_check_if_del_user_from_project_when_del_from_team()
RETURNS TRIGGER
AS $$
DECLARE

	associated_project_id INTEGER;

BEGIN
	
	SELECT T.project_id INTO associated_project_id
	FROM Team T
	WHERE T.team_id = OLD.team_id;

	IF NOT EXISTS(SELECT * FROM Works_in W NATURAL JOIN Team T
				WHERE W.user_id = OLD.user_id AND T.project_id = associated_project_id) THEN 

		DELETE 
		FROM Works_on W2 
		WHERE W2.user_id = OLD.user_id AND W2.project_id = associated_project_id;
	

	END IF;

	RETURN OLD;

END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER check_if_del_user_from_project_when_del_from_team
AFTER DELETE ON Works_in
FOR EACH ROW
EXECUTE FUNCTION fun_check_if_del_user_from_project_when_del_from_team();

-------------------------------------------------------------------------------------------------------------------------
