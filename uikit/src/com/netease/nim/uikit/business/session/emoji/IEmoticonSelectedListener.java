package com.netease.nim.uikit.business.session.emoji;

import java.io.File;

public interface IEmoticonSelectedListener {
    void onEmojiSelected(String key);

    void onStickerSelected(String categoryName, String stickerName);

    void onCollectionEmoji (File file);
}
