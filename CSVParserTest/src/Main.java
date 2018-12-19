public class Main {

	public static void main(String[] args) {
		/**
		 * Uncomment the following block to check filenames and terminate program in case any filename is inappropriate
		 */
		/*for (int i = 0; i < args.length; i++) {
			String filename = args[i];
			Pattern p = Pattern.compile("^.+\\.csv$");
			Matcher m = p.matcher(filename);
			if (!m.matches()) {
				System.out.println("argument #" + i + " (" + filename + ") is incorrect (should be *.csv filename)");
				System.out.println("Program execution aborted.");
				return;
			}
		}*/
		ParallelFileReader pfr = new ParallelFileReader();
		Thread[] threads = new Thread[args.length];
		for (int i = 0; i < args.length; i++) {
			final int j = i;
			threads[j] = new Thread() {
				@Override
				public void run() {
					pfr.readFile(args[j]);
				}
			};
			threads[j].start();
		}
		for (int i = 0; i < args.length; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException ex) {
				System.out.println("Thread #" + i + " was interrupted");
			}
		}
		pfr.printResults();
	}

}