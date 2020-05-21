package rest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import moduleData.Subject;

/**
 * REST Web Service modelling a Module Catalog.
 *
 * @author Manol
 */
@Singleton
@Path("/service")
public class ModuleCatalog {
    
    //contains all the subjects
    private HashMap<String, Subject> subjects = new HashMap<String, Subject>();

    //I am using lock to improve on my previous imperfect attemt in CW1
    private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static final Lock WRITE_LOCK = LOCK.writeLock();
    private static final Lock READ_LOCK = LOCK.readLock();

    /**
     * Creates a new instance of MainREST
     */
    public ModuleCatalog() {
    }

    /**
     * Creates a new module if such does not exist.
     * @param subject of module
     * @param level of module
     * @param code of module
     * @param name of module
     * @return string indicating success or failure
     */
    @POST
    @Produces("text/plain")
    @Path("/create/{subject}/{level}/{code}/{name}")
    public String createModule(@PathParam("subject") String subject,
            @PathParam("level") String level,
            @PathParam("code") String code,
            @PathParam("name") String name) {
        String output;

        Subject moduleSubject = new Subject(subject);
        WRITE_LOCK.lock();
        try {
            Subject temp = null;
            temp = subjects.putIfAbsent(subject, moduleSubject);
            if (temp != null) {
                moduleSubject = temp;
            }
        } finally {
            WRITE_LOCK.unlock();
        }
        Integer levelNumber = Integer.parseInt(level);
        Boolean wasCreated
                = moduleSubject.createModule(code, levelNumber, name);
        if (wasCreated) {
            output = "module created";
        } else {
            output = "failed to create module";
        }
        return output;
    }

    /**
     * Deletes a module matching the criteria.
     * @param subject of module
     * @param level of module
     * @param name of module
     * @return message notifying for success or failure
     */
    @DELETE
    @Produces("text/plain")
    @Path("/delete/{subject}/{level}/{name}")
    public String deleteModule(@PathParam("subject") String subject,
            @PathParam("level") String level,
            @PathParam("name") String name) {
        String output = "";
        Boolean wasDeleted = false;
        WRITE_LOCK.lock();
        try {
            Subject temp = subjects.get(subject);
            if (temp == null) {
                wasDeleted = false;
            } else {
                Integer levelNum = Integer.parseInt(level);
                wasDeleted = temp.deleteModule(levelNum, name);

                if (temp.isEmpty()) {
                    subjects.remove(temp.getName());
                }

            }

        } finally {
            WRITE_LOCK.unlock();
        }
        if (wasDeleted) {
            output = "module deleted";
        } else {
            output = "failed to delete module";
        }
        return output;
    }

    /**
     * Changes the active status of a module matching the criteria. 
     * "active" or "inactive"
     * @param subject of module
     * @param level of module
     * @param name of module
     * @param active marks if a module is active or inactive
     * @return message notifying for success or failure
     */
    @PUT
    @Produces("text/plain")
    @Consumes("text/plain")
    @Path("/setActive/{subject}/{level}/{name}")
    public String setActiveStatusOfModule(@PathParam("subject") String subject,
            @PathParam("level") String level, @PathParam("name") String name,
            String active) {
        String output = "";
        Boolean wasSet = false;
        READ_LOCK.lock();
        try {
            Subject temp = subjects.get(subject);
            if (temp == null) {
                wasSet = false;
            } else {
                Integer levelNum = Integer.parseInt(level);
                Boolean activeBool = false;
                if (active.equals("active")) {
                    activeBool = true;
                } else if (active.equals("inactive")) {
                    activeBool = false;
                }
                wasSet = temp.setActiveStatusOfModule(levelNum, name, activeBool);
            }

        } finally {
            READ_LOCK.unlock();
        }
        if (wasSet) {
            output = "successfully changed status";
        } else {
            output = "failed to change status";
        }
        return output;
    }

    /**
     * List all modules matching the filters.
     * @param subject of module
     * @param level of module
     * @param isActive marks if the module is active or not. "active" or "inactive"
     * @return formatted string containing all modules matching criteria
     */
    @GET
    @Produces("text/plain")
    @Path("/list")
    public String list(@DefaultValue(".+") @QueryParam("subject") String subject,
            @DefaultValue("[0-9]+") @QueryParam("level") String level,
            @DefaultValue(".+") @QueryParam("isActive") String isActive) {
        /* 
         * By using pattern matching I combined the list methods. The default 
         * values match any expected value. It also allows for selecting by any
         * parameter, but when filtering withough entering higher level filters
         * it does list empty Levels and subjects. For example when filtering 
         * only by activity.
         */
        String output = "";
        READ_LOCK.lock();
        try {
            Set<String> keySet = subjects.keySet();
            String[] keys = keySet.toArray(new String[keySet.size()]);
            Arrays.sort(keys);
            Pattern pattern = Pattern.compile(subject);
            Matcher matcher = null;
            for (String key : keys) {
                matcher = pattern.matcher(key);
                if (matcher.matches()) {
                    output += subjects.get(key).toStringWithFilter(level, isActive) + "\n";
                    //removes new lines between subjects
                    output = output.substring(0, output.length() - 1);
                }
            }
            
        } finally {
            READ_LOCK.unlock();
        }

        return output;
    }

}
