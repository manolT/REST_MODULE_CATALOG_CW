package moduleData;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class modelling a level.
 * @author Manol
 */
public class Level {
    private int id;
    
    private HashMap<String, Module> modules;
    
    private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static final Lock WRITE_LOCK = LOCK.writeLock();
    private static final Lock READ_LOCK = LOCK.readLock();
    
    public Level(int id){
        this.id = id;
        modules = new HashMap<String, Module>();
    }
    
    /**
     * Creates a new module contained in the level.
     * @param code of the module
     * @param name of the module
     * @return true if it created it successfully
     */
    public Boolean createModule(String code, String name){
        Module module = new Module(code, name);
        Boolean wasAdded = false;
        WRITE_LOCK.lock();
        try {
            Module temp = modules.putIfAbsent(name, module);
            if(temp == null){
                wasAdded = true;
            }
        } finally {
            WRITE_LOCK.unlock();
        }
        
        return wasAdded;
    }
    
    /**
     * Deletes a module matching the criteria.
     * @param name of module
     * @return boolean indicating success or failure
     */
    public Boolean deleteModule(String name){
        Boolean wasDeleted = false;
        WRITE_LOCK.lock();
        try {
            Module temp = modules.remove(name);
            if(temp != null){
                wasDeleted = true;
            }
            
        } finally {
            WRITE_LOCK.unlock();
        }
        return wasDeleted;
    }
    
    /**
     * Checks if the level is empty.
     * @return boolean indicating if the level is empty
     */
    public Boolean isEmpty(){
        Boolean isEmpty = false;
        READ_LOCK.lock();
        try {
            isEmpty = modules.isEmpty();
        } finally{
            READ_LOCK.unlock();
        }
        return isEmpty;
    }

    public int getId() {
        return id;
    }
    
    /**
     * Sets the activity status of a module matching the criteria.
     * @param name of module
     * @param active new status of module
     * @return boolean indicating success or failure
     */
    public Boolean setActiveStatusOfModule(String name, Boolean active){
        Boolean wasSet = true;
        WRITE_LOCK.lock();
        try {
            Module temp = modules.get(name);
            if(temp == null){
                wasSet = false;
            } else {
               temp.setIsActive(active);
            }
            
            
        } finally {
            WRITE_LOCK.unlock();
        }
        return wasSet;
    }
    
    /**
     * Returns the level formatted as a string and filtered.
     * @param isActive
     * @return formatted string describing the level
     */
    public String toStringWithFilter(String isActive){
        String output = "  level " + id + ":\n";
        READ_LOCK.lock();
        try {
            Set<String> keySet = modules.keySet();
            String[] keys = keySet.toArray(new String[keySet.size()]);
            Arrays.sort(keys);
            Pattern pattern = Pattern.compile(isActive);
            Matcher matcher = null;
            for(String key : keys){
                Module temp = modules.get(key);
                String activity;
                if(temp.getIsActive()){
                    activity = "active";
                } else {
                    activity = "inactive";
                }
                matcher = pattern.matcher(activity);
                if(matcher.matches()){
                    output += "    " + temp.toString() + "\n";
                }
            }
        } finally {
            READ_LOCK.unlock();
        }
        
        return output;
    }
    
}
