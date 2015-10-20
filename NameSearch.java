import javax.naming.*;
import javax.naming.directory.*;

public class NameSearch
{
    public static void main(String[] args)
    {
        try
        {
            // Get the initial context
            InitialDirContext ctx = new InitialDirContext();

            // Create the search attributes - look for a surname of Tippin
            BasicAttributes searchAttrs = new BasicAttributes();

            searchAttrs.put("sn", "Tippin");

            // Search for items with the specified attribute starting
            // at the top of the search tree
            NamingEnumeration objs = ctx.search(
                "ldap://ldap.wutka.com/o=Wutka Consulting, dc=wutka, dc=com",
                searchAttrs);

            // Loop through the objects returned in the search
            while (objs.hasMoreElements())
            {
                // Each item is a SearchResult object
                SearchResult match = (SearchResult)objs.nextElement();

                // Print out the node name
                System.out.println("Found "+match.getName()+":");

                // Get the node's attributes
                Attributes attrs = match.getAttributes();
                NamingEnumeration e = attrs.getAll();

                // Loop through the attributes
                while (e.hasMoreElements())
                {
                    // Get the next attribute
                    Attribute attr = (Attribute) e.nextElement();

                    // Print out the attribute's value(s)
                    System.out.print(attr.getID()+" = ");
                    for (int i=0; i < attr.size(); i++)
                    {
                        if (i > 0) System.out.print(", ");
                        System.out.print(attr.get(i));
                    }
                    System.out.println();
                }
                System.out.println("---------------------------------------");
            }
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }
}
