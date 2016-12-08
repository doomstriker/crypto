
package cs.fit.edu.rsa.asn1;

import cs.fit.edu.rsa.key.RSAKey;
import cs.fit.edu.rsa.key.RSAPrivateKey;

public class RsaAsn 
{

	public Encoder EncodePrivateKey(RSAPrivateKey k)
	{
		
		Encoder enc = new Encoder((byte)3).initSequence();
		enc.addToSequence(new Encoder(k.getModulus()));
		enc.addToSequence(new Encoder(k.getExponent()));
		enc.addToSequence(new Encoder(k.getPrivateExponent()));
		enc.addToSequence(new Encoder(k.getP()));
		enc.addToSequence(new Encoder(k.getQ()));
		enc.addToSequence(new Encoder(k.getdP()));
		enc.addToSequence(new Encoder(k.getdQ()));
		enc.addToSequence(new Encoder(k.getqInv()));
		
		System.out.println("Encoded: " + enc);
		//my_seq.print();
		
		return enc;
	}
	
	public Encoder EncodePublicKey(RSAKey k)
	{
		
		Encoder enc = new Encoder((byte)3).initSequence();
		enc.addToSequence(new Encoder(k.getModulus()));
		enc.addToSequence(new Encoder(k.getExponent()));
		
		System.out.println("Encoded: " + enc);
		//my_seq.print();
		
		return enc;
	}
	
	public Decoder DecodeKey(byte[] data)
	{
		Decoder dec = new Decoder(data);
		try {
			dec=dec.getContent();
		} catch (ASN1DecoderFail e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dec;
	}
	
	public RSAKey decodePublicKey(Decoder dec) {
		RSAKey key = new RSAKey();
		System.out.println(dec);
	
		key.setModulus(dec.getFirstObject(true).getInteger());
		key.setExponent(dec.getFirstObject(true).getInteger());
		System.out.println(dec);
		return key;
	}
	
	public RSAPrivateKey decodePrivateKey(Decoder dec) {
		RSAPrivateKey key = new RSAPrivateKey();
		
		System.out.println(dec);
		key.setModulus(dec.getFirstObject(true).getInteger());
		key.setExponent(dec.getFirstObject(true).getInteger());
		key.setPrivateExponent(dec.getFirstObject(true).getInteger());		
		key.setP(dec.getFirstObject(true).getInteger());
		key.setQ(dec.getFirstObject(true).getInteger());
		key.setdP(dec.getFirstObject(true).getInteger());
		key.setdQ(dec.getFirstObject(true).getInteger());	
		key.setqInv(dec.getFirstObject(true).getInteger());
		System.out.println(dec);
		return key;
	}
	}
