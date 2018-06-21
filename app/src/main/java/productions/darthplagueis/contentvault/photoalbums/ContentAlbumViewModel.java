package productions.darthplagueis.contentvault.photoalbums;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import productions.darthplagueis.contentvault.data.ContentAlbum;
import productions.darthplagueis.contentvault.data.source.album.ContentAlbumRepository;

public class ContentAlbumViewModel extends AndroidViewModel {

    private ContentAlbumRepository albumRepository;

    public ContentAlbumViewModel(@NonNull Application application) {
        super(application);
        albumRepository = new ContentAlbumRepository(application);
    }

    public LiveData<List<ContentAlbum>> getAllAlbums() {
        return albumRepository.getAllAlbums();
    }

    public LiveData<List<ContentAlbum>> getDescDateList() {
        return albumRepository.getDescDateList();
    }

    public List<String> getAlbumNames() {
        return albumRepository.getAlbumNames();
    }

    public void insert(ContentAlbum contentAlbum) {
        albumRepository.insert(contentAlbum);
    }

    public void delete(ContentAlbum contentAlbum) {
        albumRepository.delete(contentAlbum);
    }
}
