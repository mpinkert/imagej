package imagej.process.operation;

import imagej.process.query.InfoCollector;
import mpicbg.imglib.image.Image;
import mpicbg.imglib.type.numeric.RealType;

/** A QueryOperation is a read only operation that allows classes that implement the InfoCollector
 * interface to gather information about a region of an Image. Can be quite powerful when used in conjunction
 * with a SelectionFunction via queryOperation.setSelectionFunction(selectionFunc).
 */
public class QueryOperation<T extends RealType<T>> extends PositionalSingleCursorRoiOperation<T>
{
	/** the InfoCollector that defines the query */
	private InfoCollector gatherer;
	
	/** construct a QueryOperation from an image, a region definition within it, and an InfoCollector.
	 * It is the InfoCollector's job to record observations about the data.
	 */  
	public QueryOperation(Image<T> image, int[] origin, int[] span, InfoCollector gatherer)
	{
		super(image, origin, span);
		this.gatherer = gatherer;
	}

	@Override
	/** lets the InfoCollector know we're about to start the query */
	public void beforeIteration(RealType<T> type)
	{
		this.gatherer.init();
	}

	@Override
	/** gives the InfoCollector information about the current sample */
	public void insideIteration(int[] position, RealType<T> sample)
	{
		this.gatherer.collectInfo(position, sample.getRealDouble());
	}

	@Override
	/** lets the InfoCollector know we're done with the query */
	public void afterIteration()
	{
		this.gatherer.done();
	}

}
