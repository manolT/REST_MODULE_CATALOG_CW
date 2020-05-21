package moduleData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class modelling a subject.
 * @author Manol
 */
public class Subject {

    private String name;

    private HashMap<String, Level> levels;

    private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static final Lock WRITE_LOCK = LOCK.writeLock();
    private static final Lock READ_LOCK = LOCK.readLock();

    public Subject(String name) {
        this.name = name;
        this.levels = new HashMap<String, Level>();
    }

    public String getName() {
        return name;
    }

    /**
     * Creates a new module and level if needed.
     *
     * @param code of the module
     * @param level of the module
     * @param name of the module
     * @return true if the module is created successfully
     */
    public Boolean createModule(String code, int level, String name) {
        Level moduleLevel = new Level(level);
        WRITE_LOCK.lock();
        try {
            Level temp = null;
            temp = levels.putIfAbsent(level + "", moduleLevel);
            if (temp != null) {
                moduleLevel = temp;
            }
        } finally {
            WRITE_LOCK.unlock();
        }

        return moduleLevel.createModule(code, name);
    }

    /**
     * Deletes a module matching the criteria.
     * @param level of module
     * @param name of module
     * @return boolean indicating success or failure
     */
    public Boolean deleteModule(int level, String name) {
        Boolean wasDeleted = false;
        WRITE_LOCK.lock();
        try {
            Level temp = levels.get(level + "");
            if (temp == null) {
                return false;
            }

            wasDeleted = temp.deleteModule(name);
            if(temp.isEmpty()){
                levels.remove(temp.getId());
            }
            
        } finally {
            WRITE_LOCK.unlock();
        }
        
        return wasDeleted;
    }

    /**
     * Checks if the subject is empty.
     * @return boolean indicating if the subject is empty
     */
    public Boolean isEmpty(){
        Boolean isEmpty = false;
        READ_LOCK.lock();
        try {
            isEmpty = levels.isEmpty();
        } finally{
            READ_LOCK.unlock();
        }
        return isEmpty;
    }
    
    /**
     * Changes activity status of a module matching the criteria.
     * @param level of module
     * @param name of module
     * @param active indicates the new status
     * @return boolean indicating success or failure
     */
    public Boolean setActiveStatusOfModule(int level, String name, Boolean active){
        Boolean wasSet = false;
        READ_LOCK.lock();
        try {
            Level temp = levels.get(level + "");
            if(temp == null){
                wasSet = false;
            } else {
                wasSet = temp.setActiveStatusOfModule(name, active);
            }
        } finally {
            READ_LOCK.unlock();
        }
        
        return wasSet;
    }
    
    /**
     * Returns the subject formatted as a string and filtered.
     * @param level filter for level
     * @param isActive filter for activity
     * @return formatted string.
     */
    public String toStringWithFilter(String level, String isActive){
        String output = name + ":\n";
        READ_LOCK.lock();
        try {
            Set<String> keySet = levels.keySet();
            String[] keys = keySet.toArray(new String[keySet.size()]);
            Arrays.sort(keys);
            Pattern pattern = Pattern.compile(level);
            Matcher matcher = null;
            for(String key : keys){
                matcher = pattern.matcher(key + "");
                if(matcher.matches()){
                    output += levels.get(key).toStringWithFilter(isActive);
                }
            }
            
        } finally {
            READ_LOCK.unlock();
        }
        
        return output;
    }
    
}