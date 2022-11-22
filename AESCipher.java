package aes;


//cipher body
public class AESCipher {
	
	private StringBuilder plaintextBld = new StringBuilder();
	private StringBuilder ciphertextBld = new StringBuilder();
	CipherBlock stateBlock;
	Key k_Store =  new Key("abvdo10alznl>8?m");
	
	
	//no param init
	AESCipher(){
		
		this.plaintextBld.append("The quick white fox jumped over the lazy brown dog. ");
		
		
	}
	AESCipher(String plaintext){
		this.plaintextBld.append(plaintext);
		
	}
	
	//encrypt method
	//builds a key and a cipher block
	
	public void encrypt() {
		//round 0, start a new state and add round key
		//while there is more plaintext to encrypt
		while(!this.plaintextBld.isEmpty()) {
			//get the next 16 or less bytes of the plaintext
			this.stateBlock = new CipherBlock(this.get_16byte_Or_Less_Plain());
			//block state is 0 as the plainbytes were just written to the block,
			//therefore add round key
			this.stateBlock.add_Round_Key(this.k_Store.get_Key_State(0));
			//run encryption rounds
			for(int i = 1; i <= 10; i++) {
				this.encrypt_State_Round(i);
			}
			//write the block to the ciphertext builder
			this.ciphertextBld.append(this.stateBlock.out_Block());
		}
		
		
		
		
		
		
	}
	
	public void encrypt_State_Round(int round) {
			
			this.stateBlock.sub_Bytes();
			this.stateBlock.shift_Rows();
			this.stateBlock.mix_Columns();
			this.stateBlock.add_Round_Key(this.k_Store.get_Key_State(round));
	}
	
	public void decrypt() {
		//round 0, start a new state -- keyMap has been generated and indexed, can count down from
		//keymap dont need to generate key blocks
		
		//while there is more ciphertext to decrypt
		while(!this.ciphertextBld.isEmpty()) {
			
			this.stateBlock = new CipherBlock(this.get_16byte_Or_Less_Cipher());
			//new state block gets round key added first 
			this.stateBlock.add_Round_Key(this.k_Store.get_Key_State(10));
			
			//run decryptioin rounds
			for(int i = 9; i > -1; i--) {
				this.decrypt_State_Round(i);
			}
			this.plaintextBld.append((this.stateBlock.out_Block()));
		}
	}
	
	public void decrypt_State_Round(int round) {
		this.stateBlock.invshift_Rows();
		this.stateBlock.invsub_Bytes();
		this.stateBlock.add_Round_Key(this.k_Store.get_Key_State(round));
		this.stateBlock.invmix_Columns();
	}
	
	public String get_16byte_Or_Less_Plain() {
		String retStr = "";
		if(plaintextBld.isEmpty()) {
			return null;
		}
		if(plaintextBld.length() >= 16) {
			retStr = this.plaintextBld.substring(0, 15);
			this.plaintextBld.delete(0, 15);
			return retStr;
		}
		else {
			retStr = this.plaintextBld.toString();
			this.plaintextBld = new StringBuilder();
			return retStr;
		}
	}
	
	public String get_16byte_Or_Less_Cipher() {
		String retStr = "";
		if(this.ciphertextBld.isEmpty()) {
			return null;
		}
		if(this.ciphertextBld.length() >= 16) {
			retStr = this.ciphertextBld.substring(0,15);
			this.ciphertextBld.delete(0, 15);
			return retStr;
		}
		else {
			retStr = this.ciphertextBld.toString();
			this.ciphertextBld = new StringBuilder();
			return retStr;
		}
	}
	
	//getters and setters
	public void set_Plaintext(String plaintext) {
		this.plaintextBld.append(plaintext);
	}
	public String get_Plaintext() {
		return this.plaintextBld.toString();
	}
		
	public void set_Ciphertext(String ciphertext) {
		this.ciphertextBld.append(ciphertext);
	}
	public String get_Ciphertext() {
		return this.ciphertextBld.toString();
	}
	
	
	
	public void cmd_Menu() {
		for(int i = 0; i < 2; i++) {
			System.out.println("*************************************************");
		}
		System.out.println("TESTING ENCRPYTION");
		System.out.println("*************************************************");
		System.out.println("Plaintext: " + this.get_Plaintext());
		System.out.println("Encrypting...");
		this.encrypt();
		System.out.println("Complete.");
		System.out.println("Ciphertext: " + this.get_Ciphertext());
		System.out.println("*************************************************"
				+ "\n*************************************************");
		System.out.println("Decrypting...");
		this.decrypt();
		System.out.println("Complete.");
		System.out.println("Plaintext: " + this.get_Plaintext());
		
		
	}

}
