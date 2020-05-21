/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modulecatalogclient;

/**
 *
 * @author Manol
 */
public class ModuleCatalogClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ModuleCatalog client = new ModuleCatalog();
        System.out.println("Demontsating adding and listing:");
        System.out.println(client.createModule("math", "1", "MT101", "Algebra 1"));
        System.out.println(client.createModule("math", "1", "MT102", "Algebra 2"));
        System.out.println(client.createModule("math", "1", "MT103", "Algebra 3"));
        System.out.println(client.createModule("math", "2", "MT201", "Calculus 1"));
        System.out.println(client.createModule("math", "3", "MT301", "Higher Mathematics 1"));
        System.out.println(client.createModule("math", "4", "MT401", "Higher Mathematics 2"));
        System.out.println(client.createModule("math", "5", "MT501", "Higher Mathematics 1"));
        System.out.println(client.createModule("programming", "2", "CS253", "WebServices"));
        System.out.println(client.list(null, null, null));
        System.out.println("Demontsating removing empty subjects and levels:");
        System.out.println(client.deleteModule("programming", "2", "WebServices"));
        System.out.println(client.list(null, null, null));
        System.out.println("Demontsating filters:");
        System.out.println(client.list("1", "math", null));
        System.out.println(client.list(null, "math", null));
        System.out.println("Demontsating changing activity status:");
        System.out.println(client.setActiveStatusOfModule("inactive", "math", "1", "Algebra 1"));
        System.out.println(client.setActiveStatusOfModule("active", "math", "1", "Algebra 1"));
        System.out.println(client.setActiveStatusOfModule("inactive", "math", "1", "Algebra 2"));
        System.out.println(client.setActiveStatusOfModule("inactive", "math", "2", "Algebra 1"));
        System.out.println("Demontsating activity filters:");
        System.out.println(client.list(null, null, "active"));
        System.out.println(client.list(null, null, "inactive"));
        System.out.println("Demontsating delete:");
        System.out.println(client.deleteModule("math", "1", "Algebra 1"));
        System.out.println(client.deleteModule("math", "1", "Algebra 1"));
        System.out.println(client.deleteModule("math", "0", "Algebra 1"));

    }

}
