package com.airtribe.library.service;

import com.airtribe.library.model.Patron;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service responsible for managing library patrons.
 */
public class PatronService {

    private static final Logger LOGGER = Logger.getLogger(PatronService.class.getName());

    private final Map<String, Patron> patronsById = new ConcurrentHashMap<>();

    public void addPatron(Patron patron) {
        if (patron == null) {
            throw new IllegalArgumentException("Patron must not be null");
        }
        String id = patron.getId();
        if (patronsById.containsKey(id)) {
            LOGGER.log(Level.WARNING, "Attempted to add duplicate patron with id: {0}", id);
            throw new IllegalStateException("Patron with id already exists: " + id);
        }
        patronsById.put(id, patron);
        LOGGER.log(Level.INFO, "Added patron: {0}", patron);
    }

    public Optional<Patron> findById(String id) {
        return Optional.ofNullable(patronsById.get(id));
    }

    public void updateEmail(String patronId, String newEmail) {
        Patron patron = patronsById.get(patronId);
        if (patron == null) {
            LOGGER.log(Level.WARNING, "Attempted to update email for non-existing patron id: {0}", patronId);
            throw new IllegalArgumentException("Patron not found: " + patronId);
        }
        patron.setEmail(newEmail);
        LOGGER.log(Level.INFO, "Updated email for patron {0} to {1}", new Object[]{patronId, newEmail});
    }

    public void updateName(String patronId, String newName) {
        Patron patron = patronsById.get(patronId);
        if (patron == null) {
            LOGGER.log(Level.WARNING, "Attempted to update name for non-existing patron id: {0}", patronId);
            throw new IllegalArgumentException("Patron not found: " + patronId);
        }
        patron.setName(newName);
        LOGGER.log(Level.INFO, "Updated name for patron {0} to {1}", new Object[]{patronId, newName});
    }

    public Collection<Patron> getAllPatrons() {
        return Collections.unmodifiableCollection(new ArrayList<>(patronsById.values()));
    }
}


