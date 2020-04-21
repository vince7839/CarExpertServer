package com.carexpert.common;

import lombok.Data;

import java.util.List;

@Data
public class TreeNode {
    String title;
    Integer id;
    Integer level;
    List<TreeNode> children;
}
