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

	-- list of junktions as {x, y, E, W, N, S, count}
	local junctions = {}

	-- index of the last junction for each train
	local lastJunction = {}

	-- the direction taken at the last junction for each train
	local lastDirection = {}


-------------------------- init ---------------------------------

function ai.init(map, money, maximumTrains)	

	rememberMap = map
	maxTrains = maximumTrains

	-- find hotspot
    for x = 1, map.width, 1 do
        for y = 1, map.height, 1 do
            if map[x][y] == "S" then    -- if the field at [x][y] is "S" then print the coordinates on the screen:
                --print("Hotspot found at: " .. x .. ", " .. y .. "!")
                hotspots[#hotspots] = {X=x,Y=y}
				-- buyTrain(x,y)
            end
        end
    end

    while money >= 25 do        -- 25c is cost of one train
    	if #hotspots>0 then
        	point = hotspots[math.random(#hotspots)]
        else
        	point = { X=math.random(rememberMap.width), Y=math.random(rememberMap.height) }
        end
        print("new train")
        buyTrain(point.X, point.Y)
        numberOfTrains = numberOfTrains+1
        money = money - 25
    end

	print(getNumberOfLines())
end

function findJunction(x, y)
	for i=1, #junctions do
		if junctions[i].x == x and junctions[i].y == y then
			return i
		end
	end
	return 0
end


---------------------- choose direction ----------------------------

function ai.chooseDirection( train,possibleDirections )
	
	--print("possibleDirs: " .. to_string(possibleDirections))

	-- first, we have to adjust the coordinates according to the trains direction
	if train.dir == 'E' then
		train.x = train.x + 1
	elseif train.dir == 'W' then
		train.x = train.x - 1
	elseif train.dir == 'S' then
		train.y = train.y + 1
	elseif train.dir == 'N' then
		train.y = train.y - 1
	end

	-- check, if we know our junction
	thisJunction = findJunction(train.x, train.y)
	if thisJunction == 0 then

		-- unknown junction. add this junction to the list of junctions
		thisJunction = #junctions + 1
		junctions[thisJunction] = { x=train.x, y=train.y, count=0}
		-- print("junctions :" .. to_string(junctions))
	end


	-- add edges from and to the last junction
	if lastJunction[train.ID] ~= nil and lastDirection[train.ID] ~= nil then
		junctions[lastJunction[train.ID]][lastDirection[train.ID]] = thisJunction 
		junctions[thisJunction][OPPOSITE_DIRS[train.dir]] = lastJunction[train.ID]
		--print("junctions :" .. to_string(junctions))		
	end

	-- set last junction
	lastJunction[train.ID] = thisJunction

	-- if we have no passenger on board, take a random direction
	if train.passenger == nil then
		repeat
			lastDirection[train.ID] = DIRS[random(4)]
		until possibleDirections[lastDirection[train.ID]]
		return lastDirection[train.ID]
	end

	-- 
	junctions[lastJunction[train.ID]].count = junctions[lastJunction[train.ID]].count + 1

	-- choose direction by comparing the distances of the reachable junctions to destination
	--[[bestDistance = 1000
	if possibleDirections['S'] then	
		lastDirection  = 'S'
		nextJunction = junctions[thisJunction]['S']
		if nextJunction == nil then
			bestDistance = computeDistance(train.x, train.y, train.passenger.destX, train.passenger.destY)
		else
			bestDistance = computeDistance(junctions[nextJunction].x, junctions[nextJunction].y, train.passenger.destX, train.passenger.destY)
		end
	elseif possibleDirections['N'] then
		nextJunction = junctions[thisJunction]['N']
		if nextJunction == nil then
			distance = computeDistance(train.x, train.y, train.passenger.destX, train.passenger.destY)
		else
			distance = computeDistance(junctions[nextJunction].x, junctions[nextJunction].y, train.passenger.destX, train.passenger.destY)
		end
		if (distance < bestDistance) then
			bestDistance = distance
			lastDirection  = 'N'
		end
	elseif possibleDirections['W'] then
		nextJunction = junctions[thisJunction]['W']
		if nextJunction == nil then
			distance = computeDistance(train.x, train.y, train.passenger.destX, train.passenger.destY)
		else
			distance = computeDistance(junctions[nextJunction].x, junctions[nextJunction].y, train.passenger.destX, train.passenger.destY)
		end
		if (distance < bestDistance) then
			bestDistance = distance
			lastDirection  = 'W'
		end
	elseif possibleDirections['E'] then
		nextJunction = junctions[thisJunction]['E']
		if nextJunction == nil then
			distance = computeDistance(train.x, train.y, train.passenger.destX, train.passenger.destY)
		else
			distance = computeDistance(junctions[nextJunction].x, junctions[nextJunction].y, train.passenger.destX, train.passenger.destY)
		end
		if (distance < bestDistance) then
			bestDistance = distance
			lastDirection  = 'E'
		end
	end]]

	-- if we're running in a circle, choose random direction
	if junctions[thisJunction].count > 2 then
		print("PANIC!")
		dropPassenger(train)

		-- validate all junctions
		for i=1,#junctions do
			junctions[i].count = 0
		end
	end

	-- choose a direction by distance heuristic
	distX = train.passenger.destX - junctions[thisJunction].x
	distY = train.passenger.destY - junctions[thisJunction].y
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
	-- second best choice
	if eastwest then
		if distY < 0 and possibleDirections['N'] then
			lastDirection[train.ID] = 'N'
			return 'N'
		elseif distY >= 0 and possibleDirections['S'] then
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
		end
	end
	
	-- random choice
	repeat
		lastDirection[train.ID] = DIRS[random(4)]
	until possibleDirections[lastDirection[train.ID]]
	return lastDirection[train.ID]


	--print(getNumberOfLines())
end

function computeDistance (x1,y1,x2,y2)
	return math.abs (x1-x2) + math.abs (y1-y2)
end

function ai.foundPassengers(train, passengers)
	--print("foundPassengers")
	--print(getNumberOfLines())
	if #passengers == 1 then
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
	return passengers[bestPass]
end	

function ai.foundDestination(train)
	dropPassenger(train)

	-- validate all junctions
	for i=1,#junctions do
		junctions[i].count = 0
	end
end

function ai.enoughMoney(money)
	if numberOfTrains < maxTrains then
	    while money >= 25 do        -- 25c is cost of one train
	    	if #hotspots>0 then
	        	point = hotspots[math.random(#hotspots)]
	        else
	        	point = { X=math.random(rememberMap.width), Y=math.random(rememberMap.height) }
	        end
	        buyTrain(point.X, point.Y)
	        numberOfTrains = numberOfTrains+1
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

