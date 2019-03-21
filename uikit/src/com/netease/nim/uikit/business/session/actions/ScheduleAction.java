package com.netease.nim.uikit.business.session.actions;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mike on 2019/3/19.
 */


public class ScheduleAction extends BaseAction {

    private String teamId = "";

    public ScheduleAction() {
        super(R.drawable.nim_action_schedule, R.string.input_panel_schedule);
    }

    @Override
    public void onClick() {
        Team team = NimUIKit.getTeamProvider().getTeamById(sessionId);
        if (team != null) {
            teamId = team.getId();
        }
        NIMClient.getService(TeamService.class).searchTeam(teamId).setCallback(new RequestCallback<Team>() {
            @Override
            public void onSuccess(Team team) {
                // 查询成功，获得群组资料
                try {
                    String extension = team.getExtServer();
                    Log.i("content", extension);
                    if (!TextUtils.isEmpty(extension)) {
                        JSONObject jsonObject = new JSONObject(extension);
                        long courseId = jsonObject.getLong("courseId");
                        CommonUtil.ScheduleClassOnClicklistener listener = CommonUtil.scheduleClassOnClicklistener;
                        if (listener != null) {
                            listener.onClick(courseId);
                        }
                        Toast.makeText(getActivity(),courseId+"",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "课程群组数据有误，请前往我的课程进行排课！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int code) {
                // 失败
                Toast.makeText(getActivity(), code + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable exception) {
                // 错误
            }
        });

       /* NIMClient.getService(TeamService.class).queryTeam(teamId).setCallback(new RequestCallbackWrapper<Team>() {
            @Override
            public void onResult(int code, Team t, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS) {
                    try {
                        String extension = t.getExtension();
                        Log.i("content",extension);
                        if (!TextUtils.isEmpty(extension)) {
                            JSONObject jsonObject = new JSONObject(extension);
                            long courseId = jsonObject.getLong("courseId");
                            CommonUtil.ScheduleClassOnClicklistener listener = CommonUtil.scheduleClassOnClicklistener;
                            listener.onClick(courseId);
                        } else {
                            Toast.makeText(getActivity(),"课程群组数据有误，请前往我的课程进行排课！",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 失败，错误码见code
                    Toast.makeText(getActivity(),"失败",Toast.LENGTH_SHORT).show();
                }

                if (exception != null) {
                    // error
                }
            }
        });*/


    }
}
