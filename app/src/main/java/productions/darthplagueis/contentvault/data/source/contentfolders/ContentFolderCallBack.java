package productions.darthplagueis.contentvault.data.source.contentfolders;

import java.util.List;

public interface ContentFolderCallBack {

    interface GetContentFolderNamesCallBack {
        void onFolderNamesRetrieved(List<String> folderNames);
    }
}
