-- Teilebahn http://www.youtube.com/watch?v=21_DvRUiq6Q

	-- directions
	local DIRS = { 'N', 'S', 'E', 'W' }

	-- encoding the opposite directions
	local OPPOSITE_DIRS = { N='S', S='N', E='W', W='E' }

	-- local copy of map
	local rememberMap = {}

	-- maximum number of trains
	local maxTrains

	-- current number of trains
	local numberOfTrains = 0

	-- list of hotspots
	local hotspots = {}

	-- list of junktions as {x, y, E, W, N, S}
	local junctions = {}

	-- indices of the visited junctions for each train
	local visitedJunctions = {}

	-- the direction taken at the last junction for each train
	local lastDirection = {}

	-- list of Passengers
	local passengerList = {}  -- create an empty list to save the passengers in

-------------------------- ai.init ---------------------------------

function ai.init(map, money, maximumTrains)	

	rememberMap = map
	maxTrains = maximumTrains

	-- find hotspot
    for x = 1, map.width, 1 do
        for y = 1, map.height, 1 do
            if map[x][y] == "S" then    -- if the field at [x][y] is "S" then print the coordinates on the screen:
                --print("Hotspot found at: " .. x .. ", " .. y .. "!")
                hotspots[#hotspots] = {X=x,Y=y}
            end
        end
    end

    -- buy trains
    checkAndBuyTrains(money)

	print(getNumberOfLines())
end

------------------------------- ai.newPassenger ------------------------------

function ai.newPassenger(name, x, y, destX, destY)

    -- create a new table which holds the info about the new passenger:
    local passenger = {x=x, y=y, destX=destX, destY=destY}

    -- save the passenger into the global list, to "remember" him for later use.
    -- use the name as an index to easily find the passenger later on.
    passengerList[name] = passenger
    --print("numberofpasses: ".. table.getn(passengerList))
end

------------------------------- ai.passengerBoarded ------------------------------

function ai.passengerBoarded(train, passenger)
    -- set the entry in the passengerList for the passenger to nil. This is the accepted way of "deleting" the entry in Lua.
    passengerList[passenger] = nil
end

------------------------------- ai.foundPassengers ------------------------------

function ai.foundPassengers(train, passengers)
	--print("foundPassengers")
	if #passengers == 1 then
		passengerList[passengers[1]] = nil
		return passengers[1]
	end

	mindist=100
	bestPass=0

	for i=1,#passengers do
		dist = math.abs (train.x-passengers[i].destX) + math.abs (train.y-passengers[i].destY)
		if  dist < mindist then
			mindist=dist
			bestPass=i
		end
	end
	passengerList[passengers[bestPass]] = nil
	return passengers[bestPass]
end	

------------------------------- ai.foundDestination ------------------------------

function ai.foundDestination(train)
	passengerList[train.passenger.name] = nil
	dropPassenger(train)
	visitedJunctions[train.ID] = {}
end

------------------------------- ai.enoughMoney ------------------------------

function ai.enoughMoney(money)
	checkAndBuyTrains(money)
end

------------------------------- ai.blocked ------------------------------

function ai.blocked(train, possibleDirections, prevDirection)
    if prevDirection == "N" then        -- if i've tried North before, then try South, then East, then West
        if possibleDirections["S"] == true then
            return "S"
        elseif possibleDirections["E"] == true then
            return "E"
        elseif possibleDirections["W"] == true then
            return "W"
        else return "N"
        end
    elseif prevDirection == "S" then
        if possibleDirections["E"] == true then
            return "E"
        elseif possibleDirections["W"] == true then
            return "W"
        elseif possibleDirections["N"] == true then
            return "N"
        else return "S"
        end
    elseif prevDirection == "E" then
        if possibleDirections["W"] == true then
            return "W"
        elseif possibleDirections["N"] == true then
            return "N"
        elseif possibleDirections["S"] == true then
            return "S"
        else return "E"
        end
    else
        if possibleDirections["N"] == true then
            return "N"
        elseif possibleDirections["S"] == true then
            return "S"
        elseif possibleDirections["E"] == true then
            return "E"
        else return "W"
        end
    end
end

---------------------- ai.chooseDirection ----------------------------

function ai.chooseDirection( train,possibleDirections )
	
	--print("possibleDirs: " .. to_string(possibleDirections))

	-- if this is a new train, we have to init the list of visited junctions
	if visitedJunctions[train.ID] == nil then
		visitedJunctions[train.ID] = {}
	end

	-- adjust junction coordinates by the train's direction
	if train.dir == 'E' then
		train.x = train.x + 1
	elseif train.dir == 'W' then
		train.x = train.x - 1
	elseif train.dir == 'S' then
		train.y = train.y + 1
	elseif train.dir == 'N' then
		train.y = train.y - 1
	end

	-- if the junction is unown, add it to the list of junctions
	thisJunction = findJunction(train.x, train.y)
	if thisJunction == 0 then
		thisJunction = #junctions + 1
		junctions[thisJunction] = { x=train.x, y=train.y, count=0}
		-- print("junctions :" .. to_string(junctions))
	end

	-- add edges from and to the last junction
	if lastDirection[train.ID] ~= nil and #visitedJunctions[train.ID] > 1 then
		lastJunction = visitedJunctions[train.ID][#visitedJunctions[train.ID]]
		junctions[lastJunction][lastDirection[train.ID]] = thisJunction 
		junctions[thisJunction][OPPOSITE_DIRS[train.dir]] = lastJunction
		--print("junctions :" .. to_string(junctions))		
	end

	-- count, how often we have visited this junction
	count = 0
	for i=1,#visitedJunctions[train.ID] do
		if visitedJunctions[train.ID][i] == thisJunction then
			count = count + 1
		end
	end

	-- if we're running in a circle, drop the passenger and reset list of visited junctions
	if count > 2 then
		print("PANIC!")
		dropPassenger(train)
		visitedJunctions[train.ID] = {}
	end

	-- add this junction to the list of visited junctions
	visitedJunctions[train.ID][#visitedJunctions[train.ID] + 1] = thisJunction

	-- determine destination

	-- if we have no passenger on board, go to the nearest one
	
	if train.passenger ~= nil then
		destX = train.passenger.destX
		destY = train.passenger.destY
		print ("boarded passenger destination: " .. destX .. ", " .. destY)
	else
		bestDist = 100
		for name,passenger in pairs(passengerList) do
			dist = math.abs (passenger.x - train.x) + math.abs (passenger.y - train.y)
			if (dist < bestDist) then
				bestDist = dist
				bestPass = passenger
			end
		end
		if bestDist < 100 then
			destX = bestPass.x
			destY = bestPass.y
			print ("go to passenger: " .. destX .. ", " .. destY)
		elseif #hotspots > 0 then
			bestDist = 100
			for i=1,#hotspots do
				dist = math.abs (hotspots[i].x - train.x) + math.abs (hotspots[i].y - train.y)
				if (dist < bestDist) then
					bestDist = dist
					bestSpot = i
				end
			end
			destX = passengerList[bestSpot].x
			destY = passengerList[bestSpot].y
			print("hotspotDest")
		else
			destX = math.random(rememberMap.width)
			destY = math.random(rememberMap.height)
			print("randomDest")
		end
	end

	-- choose a direction by distance heuristic
	distX = destX - junctions[thisJunction].x
	distY = destY - junctions[thisJunction].y
	eastwest = math.abs (distX) > math.abs (distY)

	-- best choice
	if eastwest then
		if distX < 0 and possibleDirections['W'] then
			lastDirection[train.ID] = 'W'
			return 'W'
		elseif distX >= 0 and possibleDirections['E'] then
			lastDirection[train.ID] = 'E'
			return 'E'
		end
	else
		if distY < 0 and possibleDirections['N'] then
			lastDirection[train.ID] = 'N'
			return 'N'
		elseif distY >= 0 and possibleDirections['S'] then
			lastDirection[train.ID] = 'S'
			return 'S'
		end
	end
	-- second/third best choice
	if eastwest then
		if distY < 0 and possibleDirections['N'] then
			lastDirection[train.ID] = 'N'
			return 'N'
		elseif distY >= 0 and possibleDirections['S'] then
			lastDirection[train.ID] = 'S'
			return 'S'
		elseif distY >= 0 and possibleDirections['N'] then
			lastDirection[train.ID] = 'N'
			return 'N'
		elseif distY < 0 and possibleDirections['S'] then
			lastDirection[train.ID] = 'S'
			return 'S'
		end
	else
		if distX < 0 and possibleDirections['W'] then
			lastDirection[train.ID] = 'W'
			return 'W'
		elseif distX >= 0 and possibleDirections['E'] then
			lastDirection[train.ID] = 'E'
			return 'E'
		elseif distX >= 0 and possibleDirections['W'] then
			lastDirection[train.ID] = 'W'
			return 'W'
		elseif distX < 0 and possibleDirections['E'] then
			lastDirection[train.ID] = 'E'
			return 'E'
		end
	end
	
	-- random choice
	repeat
		lastDirection[train.ID] = DIRS[random(4)]
	until possibleDirections[lastDirection[train.ID]]
	return lastDirection[train.ID]

	--print(getNumberOfLines())
end

------------------- helper methods -----------------------

-- looks up a junction by the given coordinates
function findJunction(x, y)
	for i=1, #junctions do
		if junctions[i].x == x and junctions[i].y == y then
			return i
		end
	end
	return 0
end

-- checks, if it is possible to buy a train, and in case initializes it
function checkAndBuyTrains(money)
	if numberOfTrains < maxTrains then
	    while money >= 25 do        -- 25c is cost of one train
	    	point = nil
	    	for name,passenger in pairs(passengerList) do
		        buyTrain(passenger.x, passenger.y)
		        numberOfTrains = numberOfTrains + 1
		        money = money - 25
		        return
	    	end

	    	-- no passenger waitin, start at hotspot
	    	if #hotspots>0 then
	        	point = hotspots[math.random(#hotspots)]
	        else
	        	point = { X = math.random(rememberMap.width), Y = math.random(rememberMap.height) }
	        end
	        buyTrain(point.X, point.Y)
	        numberOfTrains = numberOfTrains + 1
	        money = money - 25
		end    
	end
end

------------------- universal table print -------------

function table_print (tt, indent, done)
  done = done or {}
  indent = indent or 0
  if type(tt) == "table" then
    local sb = {}
    for key, value in pairs (tt) do
      table.insert(sb, string.rep (" ", indent)) -- indent it
      if type (value) == "table" and not done [value] then
        done [value] = true
        table.insert(sb, "{\n");
        table.insert(sb, table_print (value, indent + 2, done))
        table.insert(sb, string.rep (" ", indent)) -- indent it
        table.insert(sb, "}\n");
      elseif "number" == type(key) then
        table.insert(sb, string.format("\"%s\"\n", tostring(value)))
      else
        table.insert(sb, string.format(
            "%s = \"%s\"\n", tostring (key), tostring(value)))
       end
    end
    return table.concat(sb)
  else
    return tt .. "\n"
  end
end

function to_string( tbl )
    if  "nil"       == type( tbl ) then
        return tostring(nil)
    elseif  "table" == type( tbl ) then
        return table_print(tbl)
    elseif  "string" == type( tbl ) then
        return tbl
    else
        return tostring(tbl)
    end
end

