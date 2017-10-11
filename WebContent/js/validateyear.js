$
		.extend(
				$.fn.validatebox.defaults.rules,
				{
					yearValidation : {
						validator : function(value, param) {
							var reg = new RegExp(param[0]);
							var compare = reg.test(value);
							if (!compare) {
								$.fn.validatebox.defaults.rules.yearValidation.message = param[1];
							} else {
								return compare;
							}
						},
						message : ''
					}
				});