package com.del.ministry.view.models.tree;

import com.del.ministry.db.Publisher;
import com.del.ministry.utils.DateUtilz;
import com.del.ministry.utils.ListUtil;
import com.del.ministry.utils.StringUtil;

import javax.swing.tree.TreeNode;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;

public class DistrictNode implements TreeNode, Comparable<DistrictNode> {

    private AreaNode parent;
    private Long districtId;
    private String districtName;

    private Publisher publisher;
    private Date appointmentFrom;

    public DistrictNode(AreaNode parent, Long districtId, String districtName, Publisher publisher, Date appointmentFrom) {
        this.parent = parent;
        this.districtId = districtId;
        this.districtName = districtName;
        this.publisher = publisher;
        this.appointmentFrom = appointmentFrom;
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return null;
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    public AreaNode getParent() {
        return parent;
    }

    @Override
    public int getIndex(TreeNode node) {
        return 0;
    }

    @Override
    public boolean getAllowsChildren() {
        return false;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public Enumeration children() {
        return Collections.emptyEnumeration();
    }

    public Long getDistrictId() {
        return districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(districtName);
        if (publisher != null) {
            sb.append(" - ").append(publisher.getFIO()).
                    append(" (").append(DateUtilz.formatDate(appointmentFrom)).append(")");
        }
        return sb.toString();
    }

    @Override
    public int compareTo(DistrictNode o) {
        int i1 = ListUtil.safeGet(StringUtil.extractNumbers(districtName), 0, 0);
        int i2 = ListUtil.safeGet(StringUtil.extractNumbers(o.districtName), 0, 0);
        return i1 - i2;
    }
}
