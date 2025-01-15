function lerpsmooth(start, end, t)
{
    var t2 = lerp(Math.pow(t,2), 1 - Math.pow(1-t,2), t);
	return lerp(start,end,t2);
}

function lerp(a, b, t)
{
    return (1 - t) * a + t * b;
}