package productions.darthplagueis.contentvault.data.source.album;

import java.util.List;

public interface ContentFolderCallBack {

    interface GetContentFolderNamesCallBack {
        void onFolderNamesRetrieved(List<String> folderNames);
    }
}
