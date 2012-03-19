using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.ServiceModel.Channels;
using System.ServiceModel.Activation;

namespace  PersonWebService
{
   [DataContract(Namespace = "")]
   public class Person
   {
		[DataMember(Order=1)]
		public  int Id;
		[DataMember(Order=2)]
		public  string FirstName;
		[DataMember(Order=3)]
		public  string LastName;
		[DataMember(Order=4)]
		public  string Email;
   }

   [CollectionDataContract(Name = "PersonList", Namespace = "")]
   public class PersonList : List<Person>
   {
   }
}
