package com.dfire.core.job;

import com.dfire.common.entity.HeraDebugHistory;
import com.dfire.common.entity.HeraJobHistory;
import com.dfire.common.util.HierarchyProperties;
import com.dfire.common.util.HeraDateTool;
import com.dfire.core.config.HeraGlobalEnvironment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: <a href="mailto:lingxiao@2dfire.com">凌霄</a>
 * @time: Created in 11:36 2018/1/10
 * @desc Job上下文, 当存在多个Job顺序处理时，通过上下文莱传递Job状态与信息
 */
@Builder
@Data
@AllArgsConstructor
public class JobContext {

    private static final String runPath = "/tmp/hera";
    //调度执行
    public static final int SCHEDULE_RUN = 1;
    //手动执行
    public static final int MANUAL_RUN = 2;
    //DEBUG执行
    public static final int DEBUG_RUN = 3;
    //系统命令执行
    public static final int SYSTEM_RUN = 4;

    private final int runType;

    private Map<String, Object> data = new HashMap<String, Object>();

    private Integer preExitCode;

    private Integer coreExitCode;

    private String workDir;

    private HierarchyProperties properties = new HierarchyProperties(new HashMap<String, String>());

    private List<Map<String, String>> resources;

    private HeraJobHistory heraJobHistory;

    private HeraDebugHistory debugHistory;

    private HeraGlobalEnvironment environment;

    public JobContext() {
        this(MANUAL_RUN);
    }

    public JobContext(int runType) {
        this.runType = runType;
    }

    public static JobContext getTempJobContext(int runType) {
        JobContext jobContext = new JobContext(runType);
        HeraJobHistory jobHistory = new HeraJobHistory();
        jobContext.setHeraJobHistory(jobHistory);
        File baseFile = new File(runPath);
        if (baseFile == null || !baseFile.exists()) {
            baseFile.mkdir();
        }
        File file = new File(runType + HeraDateTool.getToday());
        if (!file.exists()) {
            if (!file.mkdir()) {
                throw new SecurityException("create file failed,please check : " + file.getAbsolutePath());
            }
        }
        jobContext.setWorkDir(file.getAbsolutePath());
        jobContext.setProperties(new HierarchyProperties((new HashMap<String, String>())));
        return jobContext;

    }

}
