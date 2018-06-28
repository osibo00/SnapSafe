package productions.darthplagueis.contentvault;

public interface ActivityNavigator {

    void createImportIntent();

    void createDetailedView(String filePath);

    void createCarouselView();
}
