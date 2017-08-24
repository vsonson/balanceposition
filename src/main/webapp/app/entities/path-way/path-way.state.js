(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('path-way', {
            parent: 'entity',
            url: '/path-way?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PathWays'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/path-way/path-ways.html',
                    controller: 'PathWayController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('path-way-detail', {
            parent: 'path-way',
            url: '/path-way/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PathWay'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/path-way/path-way-detail.html',
                    controller: 'PathWayDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PathWay', function($stateParams, PathWay) {
                    return PathWay.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'path-way',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('path-way-detail.edit', {
            parent: 'path-way-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/path-way/path-way-dialog.html',
                    controller: 'PathWayDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PathWay', function(PathWay) {
                            return PathWay.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('path-way.new', {
            parent: 'path-way',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/path-way/path-way-dialog.html',
                    controller: 'PathWayDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                desc: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('path-way', null, { reload: 'path-way' });
                }, function() {
                    $state.go('path-way');
                });
            }]
        })
        .state('path-way.edit', {
            parent: 'path-way',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/path-way/path-way-dialog.html',
                    controller: 'PathWayDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PathWay', function(PathWay) {
                            return PathWay.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('path-way', null, { reload: 'path-way' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('path-way.delete', {
            parent: 'path-way',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/path-way/path-way-delete-dialog.html',
                    controller: 'PathWayDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PathWay', function(PathWay) {
                            return PathWay.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('path-way', null, { reload: 'path-way' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
