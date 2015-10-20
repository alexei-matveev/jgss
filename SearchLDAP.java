// javax.naming and javax.naming.directory are
// part of the JNDI component of J2SE 1.3+.
import javax.naming.directory.*;
import javax.naming.*;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Properties;
public class SearchLDAP {
    public static void main(String[] args) {
        // The search base is the level in the hierarchy
        // that our search will start at. Here was use ""
        // which indicates the very root of the directory.
        String base = "";
        // LDAP filters are sort of like a WHERE clause. It
        // is constructed in a standard way based on LDAP
        // standards. The search here is a simple one that
        // says to return any entry with an objectclass value.
        // Since all entries must contain an objectclass, all
        // entries will be returned.
        String filter = "(objectclass=*)";
        // Here we set some connection properties for JNDI.
        Properties env = new Properties();
        // The Sun provider is the most widely used JNDI
        // provider and comes with Java 1.3+
        env.put(DirContext.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");
        // The provider URL is an LDAP URL that tells JNDI
        // where it will need to connect to.
        env.put(DirContext.PROVIDER_URL,"ldap://ldap.forumsys.com:389");
        env.put(DirContext.SECURITY_AUTHENTICATION, "simple");
        env.put(DirContext.SECURITY_PRINCIPAL, "cn=read-only-admin,dc=example,dc=com");
        env.put(DirContext.SECURITY_CREDENTIALS, "password");
        try {
            // Here we create a DirContext object using
            // the environment we setup above. This
            // object will be used to communicate with
            // the server.
            DirContext dc = new InitialDirContext(env);
            System.out.println(dc.getEnvironment());
            // Above we mentioned the filter and base.
            // Another important part of the search criteria
            // is the scope. There are three scopes: base (this
            // entry only), onelevel (the direct children of this
            // entry), and subtree (this entry and all of its
            // decendents in the tree). In JNDI, OBJECT_SCOPE
            // indicates a base search.

            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            // Search for items with the specified attribute starting
            // at the top of the search tree
            NamingEnumeration objs = dc.search("ou=mathematicians,dc=example,dc=com",
                                               "(objectClass=*)", searchControls);

            // Loop through the objects returned in the search
            while (objs.hasMoreElements()) {
                // Each item is a SearchResult object
                SearchResult match = (SearchResult) objs.nextElement();

                // Print out the node name
                System.out.println("node: " + match.getName() + "");

                // Get the node's attributes
                Attributes attrs = match.getAttributes();

                NamingEnumeration e = attrs.getAll();

                // Loop through the attributes
                while (e.hasMoreElements()) {
                    // Get the next attribute
                    Attribute attr = (Attribute) e.nextElement();

                    // Print out the attribute's value(s)
                    System.out.println ("attr: " + attr.getID() + " = ");
                    for (int i=0; i < attr.size(); i++) {
                        System.out.println ("    : " + attr.get(i));
                    }
                    System.out.println();
                }
                System.out.println("---------------------------------------");
            }

            // Here we unbind from the LDAP server.
            dc.close();
        } catch (NamingException nex) {
            // A number of exceptions are subclassed from
            // NamingException. In a real application you'd
            // probably want to handle many of them
            // differently.
            System.err.println("Error: " + nex.getMessage());
        }
    }
}
