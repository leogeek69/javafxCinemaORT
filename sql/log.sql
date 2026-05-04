-- TABLE LOG (MODIFIEE : ajout colonne utilisateur)
CREATE TABLE log(
                    idlog SERIAL PRIMARY KEY,
                    tableName VARCHAR(50),
                    operation VARCHAR(50),
                    dateAction TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    ancienContenu TEXT,
                    nouveauContenu TEXT,
                    utilisateur VARCHAR(100) -- NOUVEAU : colonne utilisateur ajoutée
);

-- FONCTION (MODIFIEE : ajout de current_user dans l'INSERT)
CREATE OR REPLACE FUNCTION insert_log_function(
    p_tableName VARCHAR,
    p_operation VARCHAR,
    p_ancienContenu TEXT,
    p_nouveauContenu TEXT
) RETURNS void LANGUAGE plpgsql AS $$
BEGIN
INSERT INTO log(tableName, operation, ancienContenu, nouveauContenu, utilisateur)
VALUES (p_tableName, p_operation, p_ancienContenu, p_nouveauContenu, current_user); -- NOUVEAU : current_user ajouté
END;
$$;

-- TOUT CE QUI SUIT EST NOUVEAU (triggers cinema et franchise)
-- Les triggers section, cours, etudiant restent inchangés

-- CINEMA
-- INSERT
CREATE OR REPLACE FUNCTION trigger_cinema_create() RETURNS TRIGGER AS $$
BEGIN PERFORM insert_log_function(
    'cinema', 'INSERT', '',
    'ID: ' || NEW.id_cinema || ', Denomination: ' || NEW.denomination || ', Adresse: ' || NEW.adresse || ', Ville: ' || NEW.ville || ', IDFranchise: ' || NEW.id_franchise
);
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER cinema_create AFTER INSERT ON cinema FOR EACH ROW EXECUTE FUNCTION trigger_cinema_create();

-- UPDATE
CREATE OR REPLACE FUNCTION trigger_cinema_update() RETURNS TRIGGER AS $$
BEGIN PERFORM insert_log_function(
    'cinema', 'UPDATE',
    'ID: ' || OLD.id_cinema || ', Denomination: ' || OLD.denomination || ', Adresse: ' || OLD.adresse || ', Ville: ' || OLD.ville || ', IDFranchise: ' || OLD.id_franchise,
    'ID: ' || NEW.id_cinema || ', Denomination: ' || NEW.denomination || ', Adresse: ' || NEW.adresse || ', Ville: ' || NEW.ville || ', IDFranchise: ' || NEW.id_franchise
);
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER cinema_update AFTER UPDATE ON cinema FOR EACH ROW EXECUTE FUNCTION trigger_cinema_update();

-- DELETE
CREATE OR REPLACE FUNCTION trigger_cinema_delete() RETURNS TRIGGER AS $$
BEGIN PERFORM insert_log_function(
    'cinema', 'DELETE',
    'ID: ' || OLD.id_cinema || ', Denomination: ' || OLD.denomination || ', Adresse: ' || OLD.adresse || ', Ville: ' || OLD.ville || ', IDFranchise: ' || OLD.id_franchise,
    ''
);
RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER cinema_delete AFTER DELETE ON cinema FOR EACH ROW EXECUTE FUNCTION trigger_cinema_delete();

-- FRANCHISE
-- INSERT
CREATE OR REPLACE FUNCTION trigger_franchise_create() RETURNS TRIGGER AS $$
BEGIN PERFORM insert_log_function(
    'franchise', 'INSERT', '',
    'ID: ' || NEW.id_franchise || ', Nom: ' || NEW.nom_franchise || ', Siege: ' || NEW.siege_social || ', IDGerant: ' || NEW.id_gerant
);
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER franchise_create AFTER INSERT ON franchise FOR EACH ROW EXECUTE FUNCTION trigger_franchise_create();

-- UPDATE
CREATE OR REPLACE FUNCTION trigger_franchise_update() RETURNS TRIGGER AS $$
BEGIN PERFORM insert_log_function(
    'franchise', 'UPDATE',
    'ID: ' || OLD.id_franchise || ', Nom: ' || OLD.nom_franchise || ', Siege: ' || OLD.siege_social || ', IDGerant: ' || OLD.id_gerant,
    'ID: ' || NEW.id_franchise || ', Nom: ' || NEW.nom_franchise || ', Siege: ' || NEW.siege_social || ', IDGerant: ' || NEW.id_gerant
);
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER franchise_update AFTER UPDATE ON franchise FOR EACH ROW EXECUTE FUNCTION trigger_franchise_update();

-- DELETE
CREATE OR REPLACE FUNCTION trigger_franchise_delete() RETURNS TRIGGER AS $$
BEGIN PERFORM insert_log_function(
    'franchise', 'DELETE',
    'ID: ' || OLD.id_franchise || ', Nom: ' || OLD.nom_franchise || ', Siege: ' || OLD.siege_social || ', IDGerant: ' || OLD.id_gerant,
    ''
);
RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER franchise_delete AFTER DELETE ON franchise FOR EACH ROW EXECUTE FUNCTION trigger_franchise_delete();