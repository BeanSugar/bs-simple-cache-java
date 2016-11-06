package org.scriptonbasestar.cache.geo.tree;

public class TreeNode<E> {
	private final int size;
	private int startNo;
	private TreeNode<E>[] links;

	private E value;
	//@Getter
	public E getValue(){
		return value;
	}
	//@Setter
	public void setValue(E value){
		this.value = value;
	}

	public TreeNode(E item){
		size = 3;
		links = new TreeNode[size];
		this.value = item;
	}

	/**
	 * root 노드가 될 수 있는지 판단. null값이 1개이상 있어야함
	 * @return
	 */
	public boolean isRootable(){
		int nullcnt = 0;
		for(int i=0;i<size;i++){
			if(links[i] == null){
				nullcnt++;
			}
		}
		//root는 parent가 없어야된다.
		if(nullcnt>=1){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * root노드로 만들기
	 * @return
	 */
	public boolean makeRoot(){
		if(isRootable()) {
			for(int i=0;i<size;i++){
				if(get(i)==null){
					setParent(i);
				}
			}
			makeRootRecursively(this);
			return true;
		}
		return false;
	}

	/**
	 * this 객체의 하위객체 추적해서 root노드 만들기
	 * @param proot
	 */
	private void makeRootRecursively(TreeNode<E> proot){
		if(proot.get(1)!=null){
			TreeNode n1 = proot.get(1);
			n1.setParent(proot);
			makeRootRecursively(n1);
		}
		if(proot.get(2)!=null) {
			TreeNode n2 = proot.get(2);
			n2.setParent(proot);
			makeRootRecursively(n2);
		}
	}

	/**
	 * TreeNode인스턴드와 같은게 있으면 parent 지정하기
	 * links[0]이 아닌 links[startNo]가 parent값
	 * @param plink
	 * @return
	 */
	public boolean setParent(TreeNode<E> plink){
		if(plink == null){
			return false;
		}
		for(int i=0;i<size;i++){
			if(get(i)!=null && get(i).equals(plink)){
				return setParent(i);
			}
		}
		links[startNo] = plink;
		return true;
	}

	/**
	 * 숫자를 이용해서 TreeNode의 parent지정하기
	 * links[0]이 아닌 links[startNo]가 parent값
	 * @param index
	 * @return
	 */
	public boolean setParent(int index){
		if(index<0 && size<=index){
			return false;
		}
		this.startNo = (this.startNo+index)%size;
		return true;
	}

	/**
	 * 인덱스 번호 이용해서 노드 추가
	 * @param index
	 * @param link
	 * @return
	 */
	public TreeNode<E> put(int index, TreeNode<E> link){
		if(index<0 && size<=index){
			return null;
		}
		links[(index+startNo)%size] = link;
		if(link!=null){
			link.setParent(this);
		}
		return this;
	}

	/**
	 * 인덱스 번호 이용해서 노드 꺼내기
	 * @param index
	 * @return
	 */
	public TreeNode<E> get(int index){
		if(index<0 && size<=index){
			return null;
		}
		return links[(index+startNo)%size];
	}

	/**
	 * 모든 값 콘솔 출력
	 */
	public void showAll(){
		showAllRecursively(this);
	}

	/**
	 * 모든 값 콘솔 출력시 하위 객체 추적하기
	 * @param proot
	 */
	private void showAllRecursively(TreeNode<E> proot){
		if(proot.get(1)!=null){
			showAllRecursively(proot.get(1));
		}
		if(proot.get(2)!=null){
			showAllRecursively(proot.get(2));
		}
		System.out.println(proot.toString());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("value :");
		sb.append(value);
		sb.append(" links :");
		for(int i=0;i<size;i++){
			try{
				sb.append(" #");
				sb.append(i);
				sb.append(":");
				sb.append(get(i).value);
			}catch (Exception e){
			}
		}
		sb.append("]");
		return sb.toString();
	}
}
