package jus.aor.mobilagent.lookforhotel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;

import jus.aor.mobilagent.kernel.Agent;
import jus.aor.mobilagent.kernel.Starter;
import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel._Service;
import jus.aor.rmi.common.Hotel;
import jus.aor.rmi.common.Numero;

public class LookForHotel extends Agent {

	private static final long serialVersionUID = 1L;

	private String localisation;
	private Collection<Hotel> hotels = new LinkedList<Hotel>();
	private Map<Hotel, Numero> numbers = new HashMap<Hotel, Numero>();

	protected _Action findHotels = new _Action() {
		private static final long serialVersionUID = 1L;

		@Override
		public void execute() {
			Starter.getLogger().log(Level.INFO, "Searching for hotels");
			_Service<?> _service = LookForHotel.this.pAgentServer.getService("Hotels");
			@SuppressWarnings("unchecked")
			Collection<Hotel> _hotel = (Collection<Hotel>) _service
					.call(new Object[] { LookForHotel.this.localisation });
			LookForHotel.this.hotels.addAll(_hotel);
			Starter.getLogger().log(Level.INFO, _hotel.size() + " hotels found in " + LookForHotel.this.localisation);
		}
	};

	protected _Action findNumbers = new _Action() {
		private static final long serialVersionUID = 1L;

		@Override
		public void execute() {
			Starter.getLogger().log(Level.INFO, "Searching for hotels' number");
			_Service<?> _service = LookForHotel.this.pAgentServer.getService("Numbers");
			for (Hotel h : LookForHotel.this.hotels) {
				Numero num = (Numero) _service.call(new Object[] { h.name });
				LookForHotel.this.numbers.put(h, num);
			}
		}
	};

	public LookForHotel(Object... args) {
		this.localisation = (String) args[0];
		this.hotels = new ArrayList<>();
		this.numbers = new HashMap<>();
	}

}
