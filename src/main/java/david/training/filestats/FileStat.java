package david.training.filestats;

public class FileStat {
	public long fileSize = 0;
	public long totalFiles = 0;
	public long totalFolders = 0;

	public void merge(FileStat stats) {
		this.fileSize += stats.fileSize;
		this.totalFiles += stats.totalFiles;
		this.totalFolders += stats.totalFolders;
	}
}
