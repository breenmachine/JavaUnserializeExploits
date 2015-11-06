import java.util.Base64;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.Arrays;

public class DecodeObject{
	public static void main(String args[]) throws Exception{
		int skip=0;
		int remainder = 0;
		String b64 = args[0];
		byte[] bytes = Base64.getDecoder().decode(b64);
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		int origSize = bis.available();
		System.out.println("Data Length: "+origSize);
		Object o = null;
		while(o == null){
			try{
				bis.reset();
				bis.skip(skip);
				ObjectInputStream ois = new ObjectInputStream(bis);
				o = ois.readObject();
				
				System.out.println("Object found...");
				System.out.println(o.getClass().getName());
				System.out.println("Bytes skipped: "+skip);
				System.out.println("Bytes left: "+bis.available());
				skip = origSize - bis.available();
			}
			catch (StreamCorruptedException ode){
				skip = skip+1;
				bis.skip(1);
			}
			catch (OptionalDataException ode){
				bis.skip(1);
				skip = skip+1;
			}
			catch (ClassNotFoundException cnf)
			{
				System.out.println("Object found..."+cnf.getMessage());
				System.out.println("Bytes skipped: "+skip);
				System.out.println("Bytes left: "+bis.available());
				skip = origSize - bis.available();
			}
		}
	}
}