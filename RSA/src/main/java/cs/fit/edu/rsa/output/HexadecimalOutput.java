package cs.fit.edu.rsa.output;

import cs.fit.edu.rsa.asn1.Encoder;

public class HexadecimalOutput {

	public void LogEncoderToConsoleAsHexadecimal(Encoder enc, String category)
	{
		byte[] bytes = enc.getBytes();
		System.out.println("**********************************************");
		System.out.println("Start" + category+ " Encoder in Hexadecimals: ");
		System.out.println("**********************************************");
		for(int i=0; i<bytes.length; i++)
		{
			//String.format("%x", y)
			System.out.print(String.format("%x", bytes[i]));
		}	
		System.out.println("");
		System.out.println("**********************************************");
		System.out.println("End" + category+ " Encoder in Hexadecimals: ");
		System.out.println("**********************************************");
	}
}
