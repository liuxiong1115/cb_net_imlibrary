package com.netease.nim.demo.fileManager.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.netease.nim.demo.R;
import com.netease.nim.demo.file.browser.FileBrowserActivity;
import com.netease.nim.demo.fileManager.adapter.PublicTabViewPagerAdapter;
import com.netease.nim.demo.fileManager.fragment.FolderDataFragment;
import com.netease.nim.demo.fileManager.model.FileInfo;
import com.netease.nim.demo.fileManager.utils.FileUtil;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.widget.MyToolbar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 使用遍历文件夹的方式
 */
public class FolderActivity extends UI {

    private TabLayout tlFile;
    private ViewPager vpFile;
    private MyToolbar toolbar;

    private List<String> mTabTitle = new ArrayList<>();
    private List<Fragment> mFragment = new ArrayList<>();

    private ArrayList<FileInfo> wordData = new ArrayList<>();
    private ArrayList<FileInfo> xlsData = new ArrayList<>();
    private ArrayList<FileInfo> pptData = new ArrayList<>();
    private ArrayList<FileInfo> pdfData = new ArrayList<>();
    private ArrayList<FileInfo> zipData = new ArrayList<>();
    private ArrayList<FileInfo> allData = new ArrayList<>();

    private ProgressDialog progressDialog;

    public static void startActivityForResult(Activity activity, int reqCode) {
        Intent intent = new Intent();
        intent.setClass(activity, FolderActivity.class);
        activity.startActivityForResult(intent, reqCode);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                initData();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        tlFile = findViewById(R.id.tl_file);
        vpFile = findViewById(R.id.vp_file);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "文件浏览";
        setToolBar(R.id.toolbar, options);
        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage("正在加载中...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Thread() {
            @Override
            public void run() {
                super.run();
                getFolderData();
            }
        }.start();
    }

    /**
     * 遍历文件夹中资源
     */
    public void getFolderData() {

        scanDirNoRecursion(Environment.getExternalStorageDirectory().toString());
        handler.sendEmptyMessage(1);
    }


    private void initData() {

        mTabTitle = new ArrayList<>();
        mFragment = new ArrayList<>();
        mTabTitle.add("全部");
        mTabTitle.add("word");
        mTabTitle.add("xls");
        mTabTitle.add("ppt");
        mTabTitle.add("pdf");
        mTabTitle.add("zip");
        userRechargeListSort(wordData);
        userRechargeListSort(xlsData);
        userRechargeListSort(pptData);
        userRechargeListSort(pdfData);
        userRechargeListSort(zipData);

        allData.addAll(wordData);
        allData.addAll(xlsData);
        allData.addAll(pptData);
        allData.addAll(pdfData);
        allData.addAll(zipData);
        userRechargeListSort(allData);

        //全部
        FolderDataFragment allFragment = new FolderDataFragment();
        Bundle allBundle = new Bundle();
        ArrayList<FileInfo> all = removeDuplicates(allData);
        allBundle.putParcelableArrayList("file_data", all);
        allBundle.putBoolean("is_image", false);
        allFragment.setArguments(allBundle);
        mFragment.add(allFragment);

        //word
        FolderDataFragment wordFragment = new FolderDataFragment();
        Bundle wordBundle = new Bundle();
        ArrayList<FileInfo> word = removeDuplicates(wordData);
        wordBundle.putParcelableArrayList("file_data", word);
        wordBundle.putBoolean("is_image", false);
        wordFragment.setArguments(wordBundle);
        mFragment.add(wordFragment);

        //xls
        FolderDataFragment xlsFragment = new FolderDataFragment();
        Bundle xlsBundle = new Bundle();
        ArrayList<FileInfo> xls = removeDuplicates(xlsData);
        xlsBundle.putParcelableArrayList("file_data", xls);
        xlsBundle.putBoolean("is_image", false);
        xlsFragment.setArguments(xlsBundle);
        mFragment.add(xlsFragment);

        //ppt
        FolderDataFragment pptFragment = new FolderDataFragment();
        Bundle pptBundle = new Bundle();
        ArrayList<FileInfo> ppt = removeDuplicates(pptData);
        pptBundle.putParcelableArrayList("file_data", ppt);
        pptBundle.putBoolean("is_image", false);
        pptFragment.setArguments(pptBundle);
        mFragment.add(pptFragment);

        //pdf
        FolderDataFragment pdfFragment = new FolderDataFragment();
        Bundle pdfBundle = new Bundle();
        ArrayList<FileInfo> pdf = removeDuplicates(pdfData);
        pdfBundle.putParcelableArrayList("file_data", pdf);
        pdfBundle.putBoolean("is_image", false);
        pdfFragment.setArguments(pdfBundle);
        mFragment.add(pdfFragment);

        //zip
        FolderDataFragment zipFragment = new FolderDataFragment();
        Bundle zipBundle = new Bundle();
        ArrayList<FileInfo> zip = removeDuplicates(zipData);
        zipBundle.putParcelableArrayList("file_data", zip);
        zipBundle.putBoolean("is_image", false);
        zipFragment.setArguments(zipBundle);
        mFragment.add(zipFragment);
        FragmentManager fragmentManager = getSupportFragmentManager();

        PublicTabViewPagerAdapter tabViewPagerAdapter = new PublicTabViewPagerAdapter(fragmentManager, mFragment, mTabTitle);
        vpFile.setAdapter(tabViewPagerAdapter);

        tlFile.setupWithViewPager(vpFile);

        tlFile.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpFile.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        progressDialog.dismiss();
    }

    /**
     * 非递归
     *
     * @param path
     */
    public void scanDirNoRecursion(String path) {
        LinkedList list = new LinkedList();
        File dir = new File(path);
        File file[] = dir.listFiles();
        if (file == null) return;
        for (int i = 0; i < file.length; i++) {
            if (file[i].isDirectory())
                list.add(file[i]);
            else {
                System.out.println(file[i].getAbsolutePath());
            }
        }
        File tmp;
        while (!list.isEmpty()) {
            tmp = (File) list.removeFirst();//首个目录
            if (tmp.isDirectory()) {
                file = tmp.listFiles();
                if (file == null)
                    continue;
                for (int i = 0; i < file.length; i++) {
                    if (file[i].isDirectory())
                        list.add(file[i]);//目录则加入目录列表，关键
                    else {
//                        System.out.println(file[i]);
                        /*if (file[i].getName().endsWith(".png") || file[i].getName().endsWith(".jpg") || file[i].getName().endsWith(".gif")) {
                            //往图片集合中 添加图片的路径
                            FileInfo document = FileUtil.getFileInfoFromFile(new File(file[i].getAbsolutePath()));
                            imageData.add(document);
                        } else */
                        if (file[i].getName().endsWith(".doc") || file[i].getName().endsWith(".docx")) {
                            FileInfo document = FileUtil.getFileInfoFromFile(new File(file[i].getAbsolutePath()));
                            wordData.add(document);
                        } else if (file[i].getName().endsWith(".xls") || file[i].getName().endsWith(".xlsx")) {
                            //往图片集合中 添加图片的路径
                            FileInfo document = FileUtil.getFileInfoFromFile(new File(file[i].getAbsolutePath()));
                            xlsData.add(document);
                        } else if (file[i].getName().endsWith(".ppt") || file[i].getName().endsWith(".pptx")) {
                            //往图片集合中 添加图片的路径
                            FileInfo document = FileUtil.getFileInfoFromFile(new File(file[i].getAbsolutePath()));
                            pptData.add(document);
                        } else if (file[i].getName().endsWith(".pdf")) {
                            //往图片集合中 添加图片的路径
                            FileInfo document = FileUtil.getFileInfoFromFile(new File(file[i].getAbsolutePath()));
                            pdfData.add(document);
                        } else if (file[i].getName().endsWith(".zip") || file[i].getName().endsWith(".rar")) {
                            //往图片集合中 添加图片的路径
                            FileInfo document = FileUtil.getFileInfoFromFile(new File(file[i].getAbsolutePath()));
                            zipData.add(document);
                        } else if (file[i].getName().endsWith(".txt") || file[i].getName().endsWith(".java") ||
                                file[i].getName().endsWith(".html")) {
                            //往图片集合中 添加图片的路径
                            FileInfo document = FileUtil.getFileInfoFromFile(new File(file[i].getAbsolutePath()));
                            allData.add(document);
                        }
                    }
                }
            } else {
                System.out.println(tmp);
            }
        }
    }

    /**
     * 遍历手机所有文件 并将路径名存入集合中 参数需要 路径和集合 - 递归
     */
    public void recursionFile(File dir) {
        //得到某个文件夹下所有的文件
        File[] files = dir.listFiles();
        //文件为空
        if (files == null) {
            return;
        }
        //遍历当前文件下的所有文件
        for (File file : files) {
            //如果是文件夹
            if (file.isDirectory()) {
                //则递归(方法自己调用自己)继续遍历该文件夹
                recursionFile(file);
            } else { //如果不是文件夹 则是文件
                //如果文件名以 .mp3结尾则是mp3文件
                if (file.getName().endsWith(".doc") || file.getName().endsWith(".docx")) {
                    //往图片集合中 添加图片的路径
                    FileInfo document = FileUtil.getFileInfoFromFile(new File(file.getAbsolutePath()));
                    wordData.add(document);
                } else if (file.getName().endsWith(".xls") || file.getName().endsWith(".xlsx")) {
                    //往图片集合中 添加图片的路径
                    FileInfo document = FileUtil.getFileInfoFromFile(new File(file.getAbsolutePath()));
                    xlsData.add(document);
                } else if (file.getName().endsWith(".ppt") || file.getName().endsWith(".pptx")) {
                    //往图片集合中 添加图片的路径
                    FileInfo document = FileUtil.getFileInfoFromFile(new File(file.getAbsolutePath()));
                    pptData.add(document);
                } else if (file.getName().endsWith(".pdf")) {
                    Log.i("qqq", "pdf=======");
                    //往图片集合中 添加图片的路径
                    FileInfo document = FileUtil.getFileInfoFromFile(new File(file.getAbsolutePath()));
                    pdfData.add(document);
                }
            }
        }
    }

    /**
     * 根据时间排序
     *
     * @param list
     */
    private static void userRechargeListSort(List<FileInfo> list) {
        Collections.sort(list, new Comparator<FileInfo>() {
            @Override
            public int compare(FileInfo o1, FileInfo o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date dt1 = format.parse(o1.getTime());
                    Date dt2 = format.parse(o2.getTime());
                    if (dt1.getTime() > dt2.getTime()) {
                        return -1;
                    } else if (dt1.getTime() < dt2.getTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

    /**
     * 去重
     *
     * @param list
     * @return
     */
    public ArrayList<FileInfo> removeDuplicates(ArrayList<FileInfo> list) {
        for (int i = 0; i < list.size() - 1; i++) {//wordData
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).getFilePath().equals(list.get(j).getFilePath())) {
                    list.remove(j);
                    j--;
                }
            }
        }
        return list;
    }
}
