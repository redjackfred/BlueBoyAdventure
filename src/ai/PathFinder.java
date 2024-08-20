package ai;

import Entity.Entity;
import main.GamePanel;

import java.util.ArrayList;

public class PathFinder {
    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    public PathFinder(GamePanel gp){
        this.gp = gp;
        instantiateNodes();
    }

    public void instantiateNodes(){
        node = new Node[gp.getMaxWorldRow()][gp.getMaxWorldCol()];
        int col = 0;
        int row = 0;
        while(col < gp.getMaxWorldCol() && row < gp.getMaxWorldRow()){
            node[row][col] = new Node(col, row);
            col++;
            if(col == gp.getMaxWorldCol()){
                col = 0;
                row++;
            }
        }
    }

    public void resetNodes(){
        int col = 0;
        int row = 0;
        while( col < gp.getMaxWorldCol() && row < gp.getMaxWorldRow()){
            node[row][col].open = false;
            node[row][col].checked = false;
            node[row][col].solid = false;

            col++;
            if(col == gp.getMaxWorldRow()){
                col = 0;
                row++;
            }
        }

        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow, Entity entity){
        resetNodes();

        // Set start and goal node
        startNode = node[startRow][startCol];
        currentNode = startNode;
        goalNode = node[goalRow][goalCol];
        openList.add(currentNode);

        int col = 0;
        int row = 0;

        while(col < gp.getMaxScreenCol() && row < gp.getMaxWorldRow()){
            // Set solid node
            // Check tiles
            int tileNum = gp.tileManager.mapTileNum[gp.currentMap][col][row];
            if(gp.tileManager.tiles[tileNum].collision){
                node[row][col].solid = true;
            }
            // Check interactive tiles
            for(int i = 0; i < gp.iTile[gp.currentMap].length; i++){
                if(gp.iTile[gp.currentMap][i] != null && gp.iTile[gp.currentMap][i].destructible){
                    int itCol = gp.iTile[gp.currentMap][i].worldX/gp.tileSize;
                    int itRow = gp.iTile[gp.currentMap][i].worldY/gp.tileSize;
                    node[itRow][itCol].solid = true;
                }
            }
            // Set cost
            getCost(node[row][col]);

            col++;
            if(col == gp.getMaxWorldCol()){
                col = 0;
                row++;
            }
        }
    }

    private void getCost(Node node) {
        // G cost
        int xDist = Math.abs(node.col - startNode.col);
        int yDist = Math.abs(node.row - startNode.row);
        node.gCost = xDist + yDist;

        // H cost
        xDist = Math.abs(node.col - goalNode.col);
        yDist = Math.abs(node.row - goalNode.row);
        node.hCost = xDist + yDist;

        // F cost
        node.fCost = node.gCost + node.hCost;
    }

    public boolean search(){
        while(!goalReached && step < 500){
            int col = currentNode.col;
            int row = currentNode.row;

            // Check the current node
            currentNode.checked = true;
            openList.remove(currentNode);

            //Open the Up node
            if(row - 1 >= 0){
                openNode(node[row - 1][col]);
            }
            // Open the left node
            if(col - 1 >= 0){
                openNode(node[row][col - 1]);
            }
            // Open the left node
            if(row + 1 < gp.getMaxWorldRow()){
                openNode(node[row + 1][col]);
            }
            // Open the left node
            if(col + 1 < gp.getMaxWorldCol()){
                openNode(node[row][col + 1]);
            }

            // Find the best node
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for(int i = 0; i < openList.size(); i++){
                // Check if this node's F cost is better
                if(openList.get(i).fCost < bestNodefCost){
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                // If F cost is equal, check the G cost
                else if(openList.get(i).fCost == bestNodefCost){
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost){
                        bestNodeIndex = i;
                    }
                }
            }

            // If there's no node in the openList, end the loop
            if(openList.size() == 0){
                break;
            }

            currentNode = openList.get(bestNodeIndex);
            if(currentNode == goalNode){
                goalReached = true;
                trackThePath();
            }
            step++;
        }

        return goalReached;
    }

    private void trackThePath() {
        Node current = goalNode;

        while(current != startNode){
            pathList.add(0, current);
            current = current.parent;
        }
    }

    private void openNode(Node node) {
        if(!node.open && !node.checked && !node.solid){
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }
}
