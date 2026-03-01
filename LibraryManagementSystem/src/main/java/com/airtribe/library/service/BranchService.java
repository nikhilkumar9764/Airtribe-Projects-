package com.airtribe.library.service;

import com.airtribe.library.model.Book;
import com.airtribe.library.model.LibraryBranch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service responsible for managing library branches and inventory operations
 * such as adding copies and transferring books between branches.
 */
public class BranchService {

    private static final Logger LOGGER = Logger.getLogger(BranchService.class.getName());

    private final Map<String, LibraryBranch> branchesById = new ConcurrentHashMap<>();

    public void addBranch(LibraryBranch branch) {
        if (branch == null) {
            throw new IllegalArgumentException("Branch must not be null");
        }
        String id = branch.getId();
        if (branchesById.containsKey(id)) {
            LOGGER.log(Level.WARNING, "Attempted to add duplicate branch with id: {0}", id);
            throw new IllegalStateException("Branch with id already exists: " + id);
        }
        branchesById.put(id, branch);
        LOGGER.log(Level.INFO, "Added branch: {0}", branch);
    }

    public Optional<LibraryBranch> findById(String id) {
        return Optional.ofNullable(branchesById.get(id));
    }

    public Collection<LibraryBranch> getAllBranches() {
        return Collections.unmodifiableCollection(new ArrayList<>(branchesById.values()));
    }

    public void addCopies(String branchId, Book book, int count) {
        LibraryBranch branch = requireBranch(branchId);
        branch.addCopies(book, count);
        LOGGER.log(Level.INFO, "Added {0} copies of book {1} to branch {2}",
                new Object[]{count, book.getTitle(), branch.getName()});
    }

    public void transferCopies(String fromBranchId, String toBranchId, Book book, int count) {
        if (fromBranchId.equals(toBranchId)) {
            return;
        }
        LibraryBranch from = requireBranch(fromBranchId);
        LibraryBranch to = requireBranch(toBranchId);
        from.removeCopies(book, count);
        to.addCopies(book, count);
        LOGGER.log(Level.INFO,
                "Transferred {0} copies of book {1} from branch {2} to branch {3}",
                new Object[]{count, book.getTitle(), from.getName(), to.getName()});
    }

    private LibraryBranch requireBranch(String id) {
        LibraryBranch branch = branchesById.get(id);
        if (branch == null) {
            LOGGER.log(Level.WARNING, "Branch not found: {0}", id);
            throw new IllegalArgumentException("Branch not found: " + id);
        }
        return branch;
    }
}


