SET search_path TO applicationmanagement;

-- Use an invalid postcode and name to delete test data
DELETE FROM applicationmanagement.application WHERE contact_postcode = 'ZZ111ZZ' AND holder_name IN('TestDeleteMe','PersonDeleteMe');

