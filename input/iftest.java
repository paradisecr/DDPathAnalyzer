int a=1;
int b=2;
int count = 0;
if(a>b)
{
	count=1;
	if(a==1)
	{
		count=2;
	}else{
		count=3;
		if(a==2)
		{
			count=6;
		}
	}
}else{
	count=4;
}
count=5;