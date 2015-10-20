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
        env.put(DirContext.PROVIDER_URL,"ldap://localhost:389");
        try {
            // Here we create a DirContext object using
            // the environment we setup above. This
            // object will be used to communicate with
            // the server.
            DirContext dc = new InitialDirContext(env);
            // Above we mentioned the filter and base.
            // Another important part of the search criteria
            // is the scope. There are three scopes: base (this
            // entry only), onelevel (the direct children of this
            // entry), and subtree (this entry and all of its
            // decendents in the tree). In JNDI, OBJECT_SCOPE
            // indicates a base search.
            SearchControls sc = new SearchControls();
            sc.setSearchScope(SearchControls.OBJECT_SCOPE);
NamingEnumeration ne = null;
            // Here we actually perform the search.
            ne = dc.search(base, filter, sc);
            // We cycle through the NamingEnumeration
            // that is returned by the search.
            while (ne.hasMore()) {
                // Retrieve the result as a SearchResult
                // and print it (not very pretty). There are
                // methods for extracting the attributes and
                // values without printing, as well.
                SearchResult sr = (SearchResult) ne.next();
                System.out.println(sr.toString()+"\n");
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
